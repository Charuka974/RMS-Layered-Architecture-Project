package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderItemsDAO extends CrudDAO<OrderItems> {

    public ResultSet findByIdReturnResult(String Id) throws ClassNotFoundException, SQLException;

}
