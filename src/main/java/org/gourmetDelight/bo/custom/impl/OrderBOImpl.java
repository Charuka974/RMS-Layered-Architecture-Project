package org.gourmetDelight.bo.custom.impl;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import org.gourmetDelight.bo.custom.OrderBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.*;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.entity.Orders;
import org.gourmetDelight.entity.Payments;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBOImpl implements OrderBO {

    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENTS);
    OrderItemsDAO orderItemsDAO = (OrderItemsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDER_ITEMS);
    InventoryItemsDAO inventoryDAO = (InventoryItemsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.INVENTORY);
    OrdersDAO ordersDAO = (OrdersDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERS);


    public boolean placeOrder(Orders orderDTO, ArrayList<OrderItems> orderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException {
        // Obtain the database connection from DBConnection
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            // Disable auto-commit to manually control the transaction
            connection.setAutoCommit(false);

            // Step 1: Process payments first (payments have the primary key)
            for (Payments payment : paymentsDtos) {

                if (!paymentDAO.save(payment)) {
                    connection.rollback();
                    return false; // Rollback if payment insertion fails
                }
            }

            // Step 2: Insert the order details into the Orders table

            if (!ordersDAO.save(orderDTO)) {
                connection.rollback();
                return false; // Rollback if order insertion fails
            }

            // Step 3: Insert each order item into the OrderItems table
            for (OrderItems item : orderItemsDtos) {

                if (!orderItemsDAO.save(item)) {
                    connection.rollback();
                    return false; // Rollback if any order item insertion fails
                }

                // Step 4: Decrease inventory for each order item
                if (!inventoryDAO.decreaseFromInventoryForOrder(item.getMenuItemID(), String.valueOf(item.getQuantity()))) {
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




//=========================================================================================

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
       return ordersDAO.suggestNextID();
    }

    //Delete Order
    //=========================================================================================



    public boolean deleteOrder(String orderID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            ResultSet orderItemsResult = orderItemsDAO.findByIdReturnResult(orderID);

            while (orderItemsResult.next()) {
                String menuItemID = orderItemsResult.getString("MenuItemID");
                double quantity = orderItemsResult.getDouble("Quantity");


                if (!(inventoryDAO.increaseInventoryForOrder(menuItemID, quantity))) {
                    System.err.println("Failed to restore inventory for menu item: " + menuItemID);
                    connection.rollback();
                    return false;
                }
            }

            // get PaymentID before deleting the order because we need it to delete the payment

            ResultSet paymentResult = ordersDAO.findByIdAndReturnResult(orderID);

            String paymentID = null;
            if (paymentResult.next()) {
                paymentID = paymentResult.getString("PaymentID");
            }

            //  Delete order items of the order

            if (!orderItemsDAO.delete(orderID)) {
                System.err.println("Failed to delete order items for OrderID: " + orderID);
                connection.rollback();
                return false;
            }

            // Delete the order from the Orders

            if (!ordersDAO.delete(orderID)) {
                System.err.println("Failed to delete order for OrderID: " + orderID);
                connection.rollback();
                return false;
            }

            //  Delete the payment if PaymentID is not null
            if (paymentID != null && !paymentID.isEmpty()) {

                if (!paymentDAO.delete(paymentID)) {
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





    //=========================================================================================



    public boolean updateOrder(Orders orderDTO, ArrayList<OrderItems> newOrderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            // Disable auto-commit for transaction management
            connection.setAutoCommit(false);

            // Step 1: Get existing order items from the database
            String getOrderItemsSQL = "SELECT MenuItemID, Quantity FROM OrderItems WHERE OrderID = ?";
            ResultSet existingItemsResult = SQLUtil.execute(getOrderItemsSQL, orderDTO.getOrderID());

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
                    boolean itemDeleted = SQLUtil.execute(deleteOrderItemSQL, existingItem.getOrderID(), existingItem.getMenuItemID());

                    if (!itemDeleted || !(inventoryDAO.increaseInventoryForOrder(existingItem.getMenuItemID(), existingItem.getQuantity()))) {
                        connection.rollback();
                        return false; // Rollback if deletion or inventory restoration fails
                    }
                }
            }

            // Step 3: Update the order details
            String updateOrderSQL = "UPDATE Orders SET CustomerID = ?, UserID = ?, OrderDate = ?, TotalAmount = ?, Status = ?, OrderType = ?, ReservationID = ?, PaymentID = ? WHERE OrderID = ?";
            boolean orderUpdated = SQLUtil.execute(updateOrderSQL,
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
                boolean paymentInserted = SQLUtil.execute(insertPaymentSQL,
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
                            if (!(inventoryDAO.decreaseFromInventoryForOrder(newItem.getMenuItemID(), String.valueOf(quantityDifference)))) {
                                connection.rollback();
                                return false; // Rollback if inventory update fails
                            }
                        } else if (quantityDifference < 0) {
                            if (!(inventoryDAO.increaseInventoryForOrder(newItem.getMenuItemID(), -quantityDifference))) {
                                connection.rollback();
                                return false; // Rollback if inventory update fails
                            }
                        }

                        // Update the existing order item
                        String updateOrderItemSQL = "UPDATE OrderItems SET Quantity = ?, Price = ? WHERE OrderID = ? AND MenuItemID = ?";
                        boolean itemUpdated = SQLUtil.execute(updateOrderItemSQL,
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
                    boolean itemInserted = SQLUtil.execute(insertOrderItemSQL,
                            newItem.getOrderID(),
                            newItem.getMenuItemID(),
                            newItem.getQuantity(),
                            newItem.getPrice());

                    if (!itemInserted || !(inventoryDAO.decreaseFromInventoryForOrder(newItem.getMenuItemID(), String.valueOf(newItem.getQuantity())))) {
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


    //=========================================================================================

    @Override
    public Orders searchById(String orderId) throws ClassNotFoundException, SQLException {
        return ordersDAO.searchById(orderId);
    }

    //=========================================================================================

    public ResultSet findByIdAndReturnResult(String orderID) throws SQLException, ClassNotFoundException {
        return ordersDAO.findByIdAndReturnResult(orderID);
    }


    //=========================================================================================
    public ArrayList<OrderItems> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException {
        return ordersDAO.getOrderItemsByOrderId(orderId);
    }

    //=========================================================================================

    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType); // Use the provided alertType
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //=========================================================================================

    public void completeTheOrder(String orderID) throws SQLException, ClassNotFoundException, JRException {
        ordersDAO.completeTheOrder(orderID);
    }

    //=========================================================================================

    @Override
    public ArrayList<Orders> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Orders orderDTO) throws ClassNotFoundException, SQLException {
        return ordersDAO.save(orderDTO);
    }

    @Override
    public boolean delete(String orderID) throws ClassNotFoundException, SQLException {
        return ordersDAO.delete(orderID);
    }

    @Override
    public boolean update(Orders dto) throws ClassNotFoundException, SQLException {
        return false;
    }


    @Override
    public ArrayList<Orders> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    public String suggestNextPaymentID() throws ClassNotFoundException, SQLException {
        return paymentDAO.suggestNextID();
    }

    public Payments getPaymentById(String id) throws ClassNotFoundException, SQLException{
        return paymentDAO.searchById(id);
    }

}
