package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.CrudDAO;

import org.gourmetDelight.dto.tm.StockPurchaseTM;
import org.gourmetDelight.entity.StockPurchase;
import org.gourmetDelight.entity.StockPurchaseItems;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface PurchaseDAO extends CrudDAO<StockPurchase> {
    //public boolean save(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto);

    private boolean updateInventory(String inventoryItemID, double unitsReceived){
        return false;
    }

    //public boolean updateStockPurchase(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto);

    public String suggestNextID() throws ClassNotFoundException, SQLException;

    public String deletePurchase(String purchaseID) throws ClassNotFoundException, SQLException;

    public StockPurchaseTM searchByID(String purchaseID) throws SQLException, ClassNotFoundException;

}


