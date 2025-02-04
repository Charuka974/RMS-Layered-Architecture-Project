package org.gourmetDelight.dao.custom.impl.inventory;

import org.gourmetDelight.dao.custom.PurchaseDAO;
import org.gourmetDelight.entity.StockPurchase;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseDAOImpl implements PurchaseDAO {


    @Override
    public ArrayList<StockPurchase> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(StockPurchase purchaseDto) throws ClassNotFoundException, SQLException {
        String insertPurchaseSQL = "INSERT INTO Purchases (PurchaseID, SupplierID, TotalAmount, PurchaseDate) VALUES (?, ?, ?, ?)";
        boolean purchaseInserted = CrudUtil.execute(insertPurchaseSQL,
                purchaseDto.getPurchaseID(),
                purchaseDto.getSupplierID(),
                purchaseDto.getTotalAmount(),
                purchaseDto.getPurchaseDate());

        return purchaseInserted;
    }

    @Override
    public boolean delete(String purchaseID) throws ClassNotFoundException, SQLException {
        String deletePurchaseSql = "DELETE FROM Purchases WHERE PurchaseID = ?";
        return CrudUtil.execute(deletePurchaseSql, purchaseID);
    }


    @Override
    public boolean update(StockPurchase purchaseDto) throws ClassNotFoundException, SQLException {
        String updatePurchaseSQL = "UPDATE Purchases SET SupplierID = ?, TotalAmount = ?, PurchaseDate = ? WHERE PurchaseID = ?";
        boolean purchaseUpdated = CrudUtil.execute(updatePurchaseSQL,
                purchaseDto.getSupplierID(),
                purchaseDto.getTotalAmount(),
                purchaseDto.getPurchaseDate(),
                purchaseDto.getPurchaseID());

        return purchaseUpdated;
    }

    @Override
    public StockPurchase searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<StockPurchase> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT PurchaseID FROM Purchases ORDER BY PurchaseID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }



}


