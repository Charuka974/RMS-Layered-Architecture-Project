package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.dto.inventory.StockPurchaseItemsDto;
import org.gourmetDelight.entity.StockPurchaseItems;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PurchaseItemsDAO extends CrudDAO<StockPurchaseItems> {

    public ResultSet fetchItemFromBothIds(StockPurchaseItems purchaseItemsDto) throws ClassNotFoundException, SQLException;

    public ArrayList<StockPurchaseItems> fetchPurchaseItems(String purchaseID) throws SQLException, ClassNotFoundException;

}
