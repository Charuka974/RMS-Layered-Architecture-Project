package org.gourmetDelight.dao.custom.impl.orders;

import org.gourmetDelight.dao.custom.OrderItemsDAO;
import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItemsDAOImpl implements OrderItemsDAO {


    @Override
    public ArrayList<OrderItems> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(OrderItems item) throws ClassNotFoundException, SQLException {
        String insertOrderItemSQL = "INSERT INTO OrderItems (OrderID, MenuItemID, Quantity, Price) VALUES (?, ?, ?, ?)";
        boolean itemsInserted = CrudUtil.execute(insertOrderItemSQL,
                item.getOrderID(),
                item.getMenuItemID(),
                item.getQuantity(),
                item.getPrice());
        return itemsInserted;
    }

    @Override
    public boolean delete(String orderID) throws ClassNotFoundException, SQLException {
        String deleteOrderItemsSQL = "DELETE FROM OrderItems WHERE OrderID = ?";
        boolean itemsDeleted = CrudUtil.execute(deleteOrderItemsSQL, orderID);
        return itemsDeleted;

    }

    @Override
    public boolean update(OrderItems dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public OrderItems searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<OrderItems> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }

    @Override
    public ResultSet findByIdReturnResult(String orderID) throws ClassNotFoundException, SQLException {
        String getOrderItemsSQL = "SELECT MenuItemID, Quantity FROM OrderItems WHERE OrderID = ?";
        ResultSet orderItemsResult = CrudUtil.execute(getOrderItemsSQL, orderID);

        return orderItemsResult;
    }



}
