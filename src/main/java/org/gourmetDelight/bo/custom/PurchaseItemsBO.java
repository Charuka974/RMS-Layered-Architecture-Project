package org.gourmetDelight.bo.custom;

import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.inventory.StockPurchaseItemsDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PurchaseItemsBO extends SuperBO {

    public ArrayList<StockPurchaseItemsDto> getAll() throws ClassNotFoundException, SQLException;
    public boolean save(StockPurchaseItemsDto purchaseItemsDto) throws SQLException, ClassNotFoundException;

    public boolean delete(String purchaseID) throws ClassNotFoundException, SQLException;

    public boolean update(StockPurchaseItemsDto purchaseItemsDto) throws SQLException, ClassNotFoundException;
    public StockPurchaseItemsDto searchById(String Id) throws ClassNotFoundException, SQLException;
    public ArrayList<StockPurchaseItemsDto> searchByName(String name) throws ClassNotFoundException, SQLException;
    public String suggestNextID() throws ClassNotFoundException, SQLException;
}
