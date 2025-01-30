package org.gourmetDelight.dao.custom;


import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import org.gourmetDelight.controller.orders.OrdersController;
import org.gourmetDelight.dao.CrudDAO;
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

public interface OrdersDAO extends CrudDAO<Orders> {

    public boolean placeOrder(Orders orderDTO, ArrayList<OrderItems> orderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException;

    public boolean decreaseFromInventory(String menuItemID, String itemQuantity) throws SQLException, ClassNotFoundException;

    public String suggestNextOrderID() throws ClassNotFoundException, SQLException;

    public boolean deleteOrder(String orderID) throws SQLException, ClassNotFoundException;

    public boolean increaseInventory(String menuItemID, double quantityOfOrder) throws SQLException, ClassNotFoundException;

    public boolean updateOrder(Orders orderDTO, ArrayList<OrderItems> newOrderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException;

    public Orders getOrderById(String orderId) throws SQLException, ClassNotFoundException;

    public ArrayList<OrderItems> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException;

    public boolean validateReservation(String reservationID) throws SQLException, ClassNotFoundException;

    public void showAlert(Alert.AlertType alertType, String title, String header, String content);

    public void completeTheOrder(String orderID) throws SQLException, ClassNotFoundException, JRException;


}
