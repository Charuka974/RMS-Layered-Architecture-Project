package org.gourmetDelight.dao.custom.impl.orders;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import org.gourmetDelight.controller.orders.OrdersController;
import org.gourmetDelight.dao.custom.OrdersDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.entity.Orders;
import org.gourmetDelight.entity.Payments;
import org.gourmetDelight.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
  OrdersModel handles the process of placing orders, including inventory updates,
  order item insertion, and payment processing with proper transaction management.
 */
public class OrdersDAOImpl implements OrdersDAO {

    /*
      Places an order, updates inventory, inserts order items, and processes payments.
      Uses a transaction to ensure atomicity of all operations.


     */

    public boolean placeOrder(Orders orderDTO, ArrayList<OrderItems> orderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException {
        // Obtain the database connection from DBConnection
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            // Disable auto-commit to manually control the transaction
            connection.setAutoCommit(false);

            // Step 1: Process payments first (payments have the primary key)
            for (Payments payment : paymentsDtos) {
                String insertPaymentSQL = "INSERT INTO Payments (PaymentID, PaymentMethod, Amount, PaymentDate) VALUES (?, ?, ?, ?)";
                boolean paymentInserted = CrudUtil.execute(insertPaymentSQL,
                        payment.getPaymentID(),
                        payment.getPaymentMethod(),
                        payment.getAmount(),
                        payment.getPaymentDate());

                if (!paymentInserted) {
                    connection.rollback();
                    return false; // Rollback if payment insertion fails
                }
            }

            // Step 2: Insert the order details into the Orders table
            String insertOrderSQL = "INSERT INTO Orders (OrderID, CustomerID, UserID, OrderDate, TotalAmount, Status, OrderType, ReservationID, PaymentID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            boolean orderInserted = CrudUtil.execute(insertOrderSQL,
                    orderDTO.getOrderID(),
                    orderDTO.getCustomerID(),
                    orderDTO.getUserID(),
                    orderDTO.getOrderDate(),
                    orderDTO.getTotalAmount(),
                    orderDTO.getStatus(),
                    orderDTO.getOrderType(),
                    orderDTO.getReservationID(),
                    orderDTO.getPaymentID());

            if (!orderInserted) {
                connection.rollback();
                return false; // Rollback if order insertion fails
            }

            // Step 3: Insert each order item into the OrderItems table
            for (OrderItems item : orderItemsDtos) {
                String insertOrderItemSQL = "INSERT INTO OrderItems (OrderID, MenuItemID, Quantity, Price) VALUES (?, ?, ?, ?)";
                boolean itemsInserted = CrudUtil.execute(insertOrderItemSQL,
                        item.getOrderID(),
                        item.getMenuItemID(),
                        item.getQuantity(),
                        item.getPrice());

                if (!itemsInserted) {
                    connection.rollback();
                    return false; // Rollback if any order item insertion fails
                }

                // Step 4: Decrease inventory for each order item
                if (!decreaseFromInventory(item.getMenuItemID(), String.valueOf(item.getQuantity()))) {
                    connection.rollback();
                    return false; // Rollback if inventory update fails
                }
            }

            // Commit the transaction if all steps are successful
            connection.commit();
            return true;

        } catch (SQLException e) {
            // Rollback the transaction in case of any exception
            connection.rollback();
            System.err.println("Error during order placement: " + e.getMessage());
            return false;
        } finally {
            // Reset auto-commit to true after the operation
            connection.setAutoCommit(true);
        }
    }


    public boolean decreaseFromInventory(String menuItemID, String itemQuantity) throws SQLException, ClassNotFoundException {
        double orderQuantity = Double.parseDouble(itemQuantity);

        try {
            // Retrieve the required ingredients for the selected menu item
            String getIngredientsSQL = "SELECT InventoryItemID, QuantityNeeded FROM MenuItemIngredients WHERE MenuItemID = ?";
            ResultSet ingredientsResult = CrudUtil.execute(getIngredientsSQL, menuItemID);

            // Loop through each ingredient and check inventory availability
            while (ingredientsResult.next()) {
                String inventoryItemID = ingredientsResult.getString("InventoryItemID");
                double quantityNeededPerItem = ingredientsResult.getDouble("QuantityNeeded");
                double totalQuantityNeeded = quantityNeededPerItem * orderQuantity;

                // Check if there is enough inventory
                String checkInventorySQL = "SELECT Quantity FROM InventoryItems WHERE InventoryItemID = ?";
                ResultSet inventoryResult = CrudUtil.execute(checkInventorySQL, inventoryItemID);

                if (inventoryResult.next()) {
                    double currentQuantity = inventoryResult.getDouble("Quantity");

                    if (currentQuantity < totalQuantityNeeded) {
                        showAlert(Alert.AlertType.INFORMATION, "Info", "Insufficient inventory", "Insufficient inventory for item: " + inventoryItemID);
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Order Failed", "Inventory Item not found" );
                }

                // Update the inventory by deducting the total quantity needed
                String updateInventorySQL = "UPDATE InventoryItems SET Quantity = Quantity - ? WHERE InventoryItemID = ?";
                boolean isUpdated = CrudUtil.execute(updateInventorySQL, totalQuantityNeeded, inventoryItemID);

                if (!isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Order Failed", "Failed to place Order" );
                }
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error during inventory update: " + e.getMessage());
            throw e;
        }


    }

    /*
      Validates if the given reservation ID exists in the database.

     */


    public String suggestNextOrderID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT OrderID FROM Orders ORDER BY OrderID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("O%03d", newIdIndex);
        }
        return "O001";
    }

        //Delete Order
    //------------------------------------------------------------------------



    public boolean deleteOrder(String orderID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            //  get order items for the specified order (to restore inventory)
            String getOrderItemsSQL = "SELECT MenuItemID, Quantity FROM OrderItems WHERE OrderID = ?";
            ResultSet orderItemsResult = CrudUtil.execute(getOrderItemsSQL, orderID);

            //  get inventory quantities for each order item
            while (orderItemsResult.next()) {
                String menuItemID = orderItemsResult.getString("MenuItemID");
                double quantity = orderItemsResult.getDouble("Quantity");

                // increase inventory
                if (!increaseInventory(menuItemID, quantity)) {
                    System.err.println("Failed to restore inventory for menu item: " + menuItemID);
                    connection.rollback();
                    return false;
                }
            }

            // get PaymentID before deleting the order because we need it to delete the payment
            String getPaymentIdSQL = "SELECT PaymentID FROM Orders WHERE OrderID = ?";
            ResultSet paymentResult = CrudUtil.execute(getPaymentIdSQL, orderID);

            String paymentID = null;
            if (paymentResult.next()) {
                paymentID = paymentResult.getString("PaymentID");
            }

            //  Delete order items of the order
            String deleteOrderItemsSQL = "DELETE FROM OrderItems WHERE OrderID = ?";
            boolean itemsDeleted = CrudUtil.execute(deleteOrderItemsSQL, orderID);

            if (!itemsDeleted) {
                System.err.println("Failed to delete order items for OrderID: " + orderID);
                connection.rollback();
                return false;
            }

            // Delete the order from the Orders
            String deleteOrderSQL = "DELETE FROM Orders WHERE OrderID = ?";
            boolean orderDeleted = CrudUtil.execute(deleteOrderSQL, orderID);

            if (!orderDeleted) {
                System.err.println("Failed to delete order for OrderID: " + orderID);
                connection.rollback();
                return false;
            }

            //  Delete the payment if PaymentID is not null
            if (paymentID != null && !paymentID.isEmpty()) {
                String deletePaymentSQL = "DELETE FROM Payments WHERE PaymentID = ?";
                boolean paymentDeleted = CrudUtil.execute(deletePaymentSQL, paymentID);

                if (!paymentDeleted) {
                    System.err.println("Failed to delete payment for PaymentID: " + paymentID);
                    connection.rollback();
                    return false;
                }
            } else {
                System.out.println("No PaymentID found for OrderID: " + orderID + ". Skipping payment deletion.");
            }

            connection.commit();
            //System.out.println("Order deletion successful for OrderID: " + orderID);
            return true;

        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Error during order deletion: " + e.getMessage());
            return false;
        } finally {

            connection.setAutoCommit(true);
        }
    }



    public boolean increaseInventory(String menuItemID, double quantityOfOrder) throws SQLException, ClassNotFoundException {

        try {

            String getIngredientsSQL = "SELECT InventoryItemID, QuantityNeeded FROM MenuItemIngredients WHERE MenuItemID = ?";
            ResultSet ingredientsResult = CrudUtil.execute(getIngredientsSQL, menuItemID);


            while (ingredientsResult.next()) {
                String inventoryItemID = ingredientsResult.getString("InventoryItemID");
                double quantityNeededPerItem = ingredientsResult.getDouble("QuantityNeeded");
                double totalQuantityNeeded = quantityNeededPerItem * quantityOfOrder;


                String updateInventorySQL = "UPDATE InventoryItems SET Quantity = Quantity + ? WHERE InventoryItemID = ?";
                boolean isUpdated = CrudUtil.execute(updateInventorySQL, totalQuantityNeeded, inventoryItemID);

                if (!isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Order Failed", "Failed to place Order" );
                }
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error during inventory update: " + e.getMessage());
            throw e;
        }


    }


    //------------------------------------------------------------------------

    /*  Error during inventory restoration: Failed to restore inventory for item: M001
        Error during order deletion: Failed to restore inventory for item: M001
        */



    // Update Orders

    public boolean updateOrder(Orders orderDTO, ArrayList<OrderItems> newOrderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            // Disable auto-commit for transaction management
            connection.setAutoCommit(false);

            // Step 1: Get existing order items from the database
            String getOrderItemsSQL = "SELECT MenuItemID, Quantity FROM OrderItems WHERE OrderID = ?";
            ResultSet existingItemsResult = CrudUtil.execute(getOrderItemsSQL, orderDTO.getOrderID());

            ArrayList<OrderItems> existingOrderItemsDtos = new ArrayList<>();
            while (existingItemsResult.next()) {
                String menuItemID = existingItemsResult.getString("MenuItemID");
                double quantity = existingItemsResult.getDouble("Quantity");
                existingOrderItemsDtos.add(new OrderItems(orderDTO.getOrderID(), menuItemID, quantity, 0.0));
            }

            // Step 2: Identify items to delete and restore inventory for them
            for (OrderItems existingItem : existingOrderItemsDtos) {
                boolean itemExistsInNewList = false;
                for (OrderItems newItem : newOrderItemsDtos) {
                    if (existingItem.getMenuItemID().equals(newItem.getMenuItemID())) {
                        itemExistsInNewList = true;
                        break;
                    }
                }

                // If the item does not exist in the new list, delete it and restore inventory
                if (!itemExistsInNewList) {
                    String deleteOrderItemSQL = "DELETE FROM OrderItems WHERE OrderID = ? AND MenuItemID = ?";
                    boolean itemDeleted = CrudUtil.execute(deleteOrderItemSQL, existingItem.getOrderID(), existingItem.getMenuItemID());

                    if (!itemDeleted || !increaseInventory(existingItem.getMenuItemID(), existingItem.getQuantity())) {
                        connection.rollback();
                        return false; // Rollback if deletion or inventory restoration fails
                    }
                }
            }

            // Step 3: Update the order details
            String updateOrderSQL = "UPDATE Orders SET CustomerID = ?, UserID = ?, OrderDate = ?, TotalAmount = ?, Status = ?, OrderType = ?, ReservationID = ?, PaymentID = ? WHERE OrderID = ?";
            boolean orderUpdated = CrudUtil.execute(updateOrderSQL,
                    orderDTO.getCustomerID(),
                    orderDTO.getUserID(),
                    orderDTO.getOrderDate(),
                    orderDTO.getTotalAmount(),
                    orderDTO.getStatus(),
                    orderDTO.getOrderType(),
                    orderDTO.getReservationID(),
                    orderDTO.getPaymentID(),
                    orderDTO.getOrderID());

            if (!orderUpdated) {
                connection.rollback();
                return false; // Rollback if order update fails
            }


            // Step 4: Process payments
            for (Payments payment : paymentsDtos) {
                String insertPaymentSQL = "INSERT INTO Payments (PaymentID, PaymentMethod, Amount, PaymentDate) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE PaymentMethod = VALUES(PaymentMethod), Amount = VALUES(Amount), PaymentDate = VALUES(PaymentDate)";
                boolean paymentInserted = CrudUtil.execute(insertPaymentSQL,
                        payment.getPaymentID(),
                        payment.getPaymentMethod(),
                        payment.getAmount(),
                        payment.getPaymentDate());

                if (!paymentInserted) {
                    connection.rollback();
                    return false; // Rollback if payment processing fails
                }
            }

            // Step 5: Insert or update new order items and adjust inventory
            for (OrderItems newItem : newOrderItemsDtos) {
                // Check if the item already exists in the existing order items list
                boolean itemExists = false;
                for (OrderItems existingItem : existingOrderItemsDtos) {
                    if (existingItem.getMenuItemID().equals(newItem.getMenuItemID())) {
                        itemExists = true;
                        double quantityDifference = newItem.getQuantity() - existingItem.getQuantity();

                        // Adjust inventory based on the quantity difference
                        if (quantityDifference > 0) {
                            if (!decreaseFromInventory(newItem.getMenuItemID(), String.valueOf(quantityDifference))) {
                                connection.rollback();
                                return false; // Rollback if inventory update fails
                            }
                        } else if (quantityDifference < 0) {
                            if (!increaseInventory(newItem.getMenuItemID(), -quantityDifference)) {
                                connection.rollback();
                                return false; // Rollback if inventory update fails
                            }
                        }

                        // Update the existing order item
                        String updateOrderItemSQL = "UPDATE OrderItems SET Quantity = ?, Price = ? WHERE OrderID = ? AND MenuItemID = ?";
                        boolean itemUpdated = CrudUtil.execute(updateOrderItemSQL,
                                newItem.getQuantity(),
                                newItem.getPrice(),
                                newItem.getOrderID(),
                                newItem.getMenuItemID());

                        if (!itemUpdated) {
                            connection.rollback();
                            return false; // Rollback if item update fails
                        }
                        break;
                    }
                }

                // If the item does not exist, insert it as a new order item
                if (!itemExists) {
                    String insertOrderItemSQL = "INSERT INTO OrderItems (OrderID, MenuItemID, Quantity, Price) VALUES (?, ?, ?, ?)";
                    boolean itemInserted = CrudUtil.execute(insertOrderItemSQL,
                            newItem.getOrderID(),
                            newItem.getMenuItemID(),
                            newItem.getQuantity(),
                            newItem.getPrice());

                    if (!itemInserted || !decreaseFromInventory(newItem.getMenuItemID(), String.valueOf(newItem.getQuantity()))) {
                        connection.rollback();
                        return false; // Rollback if insertion or inventory update fails
                    }
                }
            }

            // Commit the transaction if all operations are successful
            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Error during order update: " + e.getMessage());
            return false;
        } finally {
            // Reset auto-commit to true
            connection.setAutoCommit(true);
        }
    }


    // search orders

    public Orders getOrderById(String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM Orders WHERE OrderID = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, orderId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Orders(
                        rs.getString("OrderID"),
                        rs.getString("CustomerID"),
                        rs.getString("UserID"),
                        rs.getDate("OrderDate").toLocalDate(), // Adjust the date field according to your database
                        rs.getDouble("TotalAmount"),
                        rs.getString("Status"),
                        rs.getString("OrderType"),
                        rs.getString("ReservationID"),
                        rs.getString("PaymentID")
                );
            }
        }

        return null;
    }

    public ArrayList<OrderItems> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        ArrayList<OrderItems> orderItems = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE OrderID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, orderId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                orderItems.add(new OrderItems(
                        rs.getString("OrderID"),
                        rs.getString("MenuItemID"),
                        rs.getDouble("Quantity"),
                        rs.getDouble("Price")
                ));
            }
        }
        return orderItems;
    }


    public boolean validateReservation(String reservationID) throws SQLException, ClassNotFoundException {
        if (reservationID == null) return true;
        String query = "SELECT COUNT(*) FROM Reservations WHERE ReservationID = ?";
        ResultSet resultSet = CrudUtil.execute(query, reservationID);
        return resultSet.next() && resultSet.getInt(1) > 0;
    }

    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType); // Use the provided alertType
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void completeTheOrder(String orderID) throws SQLException, ClassNotFoundException, JRException {
        if (orderID == null) {
            showAlert(Alert.AlertType.WARNING, "info", "Order ID is not found", "Failed to complete Order" );
        }else {
            String query = "UPDATE Orders SET Status = 'Completed' WHERE OrderID = ?";
            if(CrudUtil.execute(query, orderID)){
                showAlert(Alert.AlertType.INFORMATION, "info", "Order Completed", "Completed the order" );
                OrdersController ordersController = new OrdersController();
                ordersController.showTheBill(orderID);
            }else{
                showAlert(Alert.AlertType.WARNING, "warning", "Order Completion Failed", "Failed to complete Order" );
            }

        }
    }


    @Override
    public ArrayList<Orders> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Orders dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean delete(String Id) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean update(Orders dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public Orders searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<Orders> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }
}
