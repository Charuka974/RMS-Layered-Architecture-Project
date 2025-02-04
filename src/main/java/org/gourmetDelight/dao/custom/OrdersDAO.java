package org.gourmetDelight.dao.custom;


import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.entity.Orders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersDAO extends CrudDAO<Orders> {

    public ArrayList<OrderItems> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException;


    public void showAlert(Alert.AlertType alertType, String title, String header, String content);

    public void completeTheOrder(String orderID) throws SQLException, ClassNotFoundException, JRException;

    public ResultSet findByIdAndReturnResult(String orderID) throws SQLException, ClassNotFoundException;


}
