package org.gourmetDelight.dao.custom.impl.inventory;

import org.gourmetDelight.dao.custom.PurchaseItemsDAO;
import org.gourmetDelight.entity.StockPurchaseItems;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseItemsDAOImpl implements PurchaseItemsDAO {

    @Override
    public ArrayList<StockPurchaseItems> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    public boolean save(StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException {
            String insertPurchaseItemSQL = "INSERT INTO PurchaseItems (PurchaseID, InventoryItemID, Unit, UnitPrice, UnitsBought, Status) VALUES (?, ?, ?, ?, ?, ?)";
            return SQLUtil.execute(insertPurchaseItemSQL,
                    purchaseItemsDto.getPurchaseID(),
                    purchaseItemsDto.getInventoryItemID(),
                    purchaseItemsDto.getUnitPerPrice(),
                    purchaseItemsDto.getUnitPrice(),
                    purchaseItemsDto.getUnitsBought(),
                    purchaseItemsDto.getStatus());
        }

        public boolean delete(String purchaseID) throws ClassNotFoundException, SQLException {
            String query = "DELETE FROM PurchaseItems WHERE PurchaseID = ?";
            return SQLUtil.execute(query, purchaseID);
        }


        public boolean update(StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException {
            String updatePurchaseItemSQL = "UPDATE PurchaseItems SET Unit = ?, UnitPrice = ?, UnitsBought = ?, Status = ? WHERE PurchaseID = ? AND InventoryItemID = ?";
            return SQLUtil.execute(updatePurchaseItemSQL,
                    purchaseItemsDto.getUnitPerPrice(),
                    purchaseItemsDto.getUnitPrice(),
                    purchaseItemsDto.getUnitsBought(),
                    purchaseItemsDto.getStatus(),
                    purchaseItemsDto.getPurchaseID(),
                    purchaseItemsDto.getInventoryItemID());
        }

    @Override
    public StockPurchaseItems searchById(String id) throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM PurchaseItems WHERE inventoryItemID = ?";
        ResultSet resultSet = SQLUtil.execute(query, id);

        if (resultSet.next()) {  // Move cursor to first row
            return new StockPurchaseItems(
                    resultSet.getString("InventoryItemID"),
                    resultSet.getString("purchaseID"),
                    resultSet.getDouble("Unit"),
                    resultSet.getDouble("UnitPrice"),
                    resultSet.getDouble("UnitsBought"),
                    resultSet.getString("Status")
            );
        }
        return null; // If no record found, return null
    }



    @Override
    public ArrayList<StockPurchaseItems> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }

    public ResultSet fetchItemFromBothIds(StockPurchaseItems purchaseItemsDto) throws ClassNotFoundException, SQLException {
        String fetchItemSQL = "SELECT Status, UnitsBought FROM PurchaseItems WHERE PurchaseID = ? AND InventoryItemID = ?";
        return SQLUtil.execute(fetchItemSQL, purchaseItemsDto.getPurchaseID(), purchaseItemsDto.getInventoryItemID());

    }

    // Fetch purchase items based on purchaseID
    public ArrayList<StockPurchaseItems> fetchPurchaseItems(String purchaseID) throws SQLException, ClassNotFoundException {
        String fetchItemsSql = "SELECT * FROM PurchaseItems WHERE PurchaseID = ?";
        ResultSet resultSet = SQLUtil.execute(fetchItemsSql, purchaseID);
        ArrayList<StockPurchaseItems> items = new ArrayList<>();
        while (resultSet.next()) {
            StockPurchaseItems item = new StockPurchaseItems(
                    resultSet.getString("InventoryItemID"),
                    resultSet.getString("purchaseID"),
                    resultSet.getDouble("Unit"),
                    resultSet.getDouble("UnitPrice"),
                    resultSet.getDouble("UnitsBought"),
                    resultSet.getString("Status")
            );
            items.add(item);
        }
        return items;
    }



}
