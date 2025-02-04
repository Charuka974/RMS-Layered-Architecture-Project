package org.gourmetDelight.bo.custom;

import javafx.collections.ObservableList;
import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.custom.StockPurchaseDTOCustom;
import org.gourmetDelight.entity.StockPurchase;
import org.gourmetDelight.entity.StockPurchaseItems;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PurchaseBO extends SuperBO {


    public ArrayList<StockPurchase> getAll() throws ClassNotFoundException, SQLException;


    public boolean save(StockPurchase dto) throws ClassNotFoundException, SQLException;


    public boolean delete(String Id) throws ClassNotFoundException, SQLException;


    public boolean update(StockPurchase dto) throws ClassNotFoundException, SQLException;


    public StockPurchase searchById(String Id) throws ClassNotFoundException, SQLException;


    public ArrayList<StockPurchase> searchByName(String name) throws ClassNotFoundException, SQLException;


    public boolean save(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException;

    public boolean updateStockPurchase(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException;

    public String suggestNextID() throws ClassNotFoundException, SQLException;


    public String deletePurchase(String purchaseID) throws ClassNotFoundException, SQLException;

    public StockPurchaseDTOCustom searchByIdReturnTM(String Id) throws ClassNotFoundException, SQLException;


    public ObservableList<StockPurchaseDTOCustom> getAllStockPurchases() throws ClassNotFoundException, SQLException;

}
