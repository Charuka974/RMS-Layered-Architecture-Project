package org.gourmetDelight.dao.custom.impl.inventory;

import org.gourmetDelight.dao.custom.PurchaseItemsDAO;
import org.gourmetDelight.dto.inventory.StockPurchaseItemsDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseItemsDAOImpl implements PurchaseItemsDAO {

    @Override
    public ArrayList<StockPurchaseItemsDto> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    public boolean save(StockPurchaseItemsDto purchaseItemsDto) throws SQLException, ClassNotFoundException {
            String insertPurchaseItemSQL = "INSERT INTO PurchaseItems (PurchaseID, InventoryItemID, Unit, UnitPrice, UnitsBought, Status) VALUES (?, ?, ?, ?, ?, ?)";
            return CrudUtil.execute(insertPurchaseItemSQL,
                    purchaseItemsDto.getPurchaseID(),
                    purchaseItemsDto.getInventoryItemID(),
                    purchaseItemsDto.getUnitPerPrice(),
                    purchaseItemsDto.getUnitPrice(),
                    purchaseItemsDto.getUnitsBought(),
                    purchaseItemsDto.getStatus());
        }

        public boolean delete(String purchaseID) throws ClassNotFoundException, SQLException {
            String fetchItemsSql = "SELECT InventoryItemID, UnitsBought, Status FROM PurchaseItems WHERE PurchaseID = ?";
            ResultSet resultSet = CrudUtil.execute(fetchItemsSql, purchaseID);

            while (resultSet.next()) {
                String itemID = resultSet.getString("InventoryItemID");
                double unitsBought = resultSet.getDouble("UnitsBought");
                String status = resultSet.getString("Status");

                if ("Received".equalsIgnoreCase(status)) {
                    String updateInventorySql = "UPDATE InventoryItems SET Quantity = Quantity - ? WHERE InventoryItemID = ?";
                    boolean updateInventoryResult = CrudUtil.execute(updateInventorySql, unitsBought, itemID);

                    if (!updateInventoryResult) {
                        return false;
                    }
                }
            }

            String deleteItemsSql = "DELETE FROM PurchaseItems WHERE PurchaseID = ?";
            boolean deleteItemsResult = CrudUtil.execute(deleteItemsSql, purchaseID);

            return deleteItemsResult;
        }

        public boolean update(StockPurchaseItemsDto purchaseItemsDto) throws SQLException, ClassNotFoundException {
            String updatePurchaseItemSQL = "UPDATE PurchaseItems SET Unit = ?, UnitPrice = ?, UnitsBought = ?, Status = ? WHERE PurchaseID = ? AND InventoryItemID = ?";
            return CrudUtil.execute(updatePurchaseItemSQL,
                    purchaseItemsDto.getUnitPerPrice(),
                    purchaseItemsDto.getUnitPrice(),
                    purchaseItemsDto.getUnitsBought(),
                    purchaseItemsDto.getStatus(),
                    purchaseItemsDto.getPurchaseID(),
                    purchaseItemsDto.getInventoryItemID());
        }

    @Override
    public StockPurchaseItemsDto searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<StockPurchaseItemsDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }


}
