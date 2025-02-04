package org.gourmetDelight.dao.custom.impl.orders;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import org.gourmetDelight.controller.orders.OrdersController;
import org.gourmetDelight.dao.custom.InventoryItemsDAO;
import org.gourmetDelight.dao.custom.OrderItemsDAO;
import org.gourmetDelight.dao.custom.OrdersDAO;
import org.gourmetDelight.dao.custom.PaymentDAO;
import org.gourmetDelight.dao.custom.impl.inventory.InventoryItemsDAOImpl;
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


public class OrdersDAOImpl implements OrdersDAO {

//=========================================================================================

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
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


    //=========================================================================================

    @Override
    public Orders searchById(String orderId) throws ClassNotFoundException, SQLException {
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

    //=========================================================================================

    public ResultSet findByIdAndReturnResult(String orderID) throws SQLException, ClassNotFoundException {
        String getPaymentIdSQL = "SELECT PaymentID FROM Orders WHERE OrderID = ?";
        ResultSet paymentResult = CrudUtil.execute(getPaymentIdSQL, orderID);
        return paymentResult;
    }


    //=========================================================================================
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

    //=========================================================================================

    @Override
    public ArrayList<Orders> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Orders orderDTO) throws ClassNotFoundException, SQLException {
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
        return orderInserted;
    }

    @Override
    public boolean delete(String orderID) throws ClassNotFoundException, SQLException {
        String deleteOrderSQL = "DELETE FROM Orders WHERE OrderID = ?";
        boolean orderDeleted = CrudUtil.execute(deleteOrderSQL, orderID);
        return orderDeleted;
    }

    @Override
    public boolean update(Orders dto) throws ClassNotFoundException, SQLException {
        return false;
    }


    @Override
    public ArrayList<Orders> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

}
