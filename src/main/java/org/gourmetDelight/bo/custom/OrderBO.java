package org.gourmetDelight.bo.custom;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JRException;
import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.orders.OrderItemsDto;
import org.gourmetDelight.dto.orders.OrdersDto;
import org.gourmetDelight.dto.orders.PaymentsDto;
import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.entity.Orders;
import org.gourmetDelight.entity.Payments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderBO extends SuperBO {

    public boolean placeOrder(OrdersDto orderDTO, ArrayList<OrderItemsDto> orderItemsDtos, ArrayList<PaymentsDto> paymentsDtos) throws SQLException, ClassNotFoundException;
    public String suggestNextID() throws ClassNotFoundException, SQLException;
    public boolean deleteOrder(String orderID) throws SQLException, ClassNotFoundException;

    public boolean updateOrder(Orders orderDTO, ArrayList<OrderItems> newOrderItemsDtos, ArrayList<Payments> paymentsDtos) throws SQLException, ClassNotFoundException;
    public Orders searchById(String orderId) throws ClassNotFoundException, SQLException;

    public ResultSet findByIdAndReturnResult(String orderID) throws SQLException, ClassNotFoundException;
    public ArrayList<OrderItems> getOrderItemsByOrderId(String orderId) throws SQLException, ClassNotFoundException;

    public void showAlert(Alert.AlertType alertType, String title, String header, String content);
    public void completeTheOrder(String orderID) throws SQLException, ClassNotFoundException, JRException;
    public ArrayList<Orders> getAll() throws ClassNotFoundException, SQLException;
    public boolean save(Orders orderDTO) throws ClassNotFoundException, SQLException;
    public boolean delete(String orderID) throws ClassNotFoundException, SQLException;
    public boolean update(Orders dto) throws ClassNotFoundException, SQLException;
    public ArrayList<Orders> searchByName(String name) throws ClassNotFoundException, SQLException;

    public String suggestNextPaymentID() throws ClassNotFoundException, SQLException;
    public Payments getPaymentById(String id) throws ClassNotFoundException, SQLException;

}
