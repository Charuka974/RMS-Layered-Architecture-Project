package org.gourmetDelight.dao.custom.impl.inventory;

import org.gourmetDelight.dao.custom.InventoryItemsDAO;
import org.gourmetDelight.entity.InventoryItem;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryItemsDAOImpl implements InventoryItemsDAO {

    public ArrayList<InventoryItem> getAll() throws SQLException, ClassNotFoundException {

        String query = "SELECT InventoryItemID, Name, Description, Quantity, UnitsMeasured FROM InventoryItems";

        ResultSet resultSet = CrudUtil.execute(query);

        ArrayList<InventoryItem> itemList = new ArrayList<>();

        while (resultSet.next()) {

            String inventoryItemId = resultSet.getString("InventoryItemID");
            String name = resultSet.getString("Name");
            String description = resultSet.getString("Description");
            double quantity = resultSet.getDouble("Quantity");
            String unit = resultSet.getString("UnitsMeasured");

            InventoryItem item = new InventoryItem(inventoryItemId, name, description, quantity, unit);
            itemList.add(item);
        }

        resultSet.close();

        return itemList;
    }


    public boolean save(InventoryItem item) throws SQLException, ClassNotFoundException {

        String query = "INSERT INTO InventoryItems (InventoryItemID, Name, Description, Quantity, UnitsMeasured) VALUES (?, ?, ?, ?, ?)";

        Boolean result = CrudUtil.execute(query,
                item.getInventoryItemId(),
                item.getName(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnit()
        );
        return result;

    }


    public boolean update(InventoryItem item) throws SQLException, ClassNotFoundException {

        String query = "UPDATE InventoryItems SET Name = ?, Description = ?, Quantity = ?, UnitsMeasured = ? WHERE InventoryItemID = ?";

        Boolean result = CrudUtil.execute(query,
                item.getName(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnit(),
                item.getInventoryItemId()
        );
        return result;

    }


    public boolean delete(String itemId) throws SQLException, ClassNotFoundException {

        String query = "DELETE FROM InventoryItems WHERE InventoryItemID = ?";

        Boolean result = CrudUtil.execute(query, itemId);
        return result;
    }

    public InventoryItem searchById(String itemId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM InventoryItems WHERE InventoryItemID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, itemId);

        if (resultSet.next()) {
            InventoryItem item = new InventoryItem(
                    resultSet.getString("InventoryItemID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Description"),
                    resultSet.getDouble("Quantity"),
                    resultSet.getString("UnitsMeasured")
            );

            resultSet.close();
            return item;
        } else {
            resultSet.close();
            return null;
        }

    }

    public ArrayList<InventoryItem> searchByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM InventoryItems WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<InventoryItem> itemDtos = new ArrayList<>();

        while (resultSet.next()) {
            InventoryItem dto = new InventoryItem(
                    resultSet.getString("InventoryItemID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Description"),
                    resultSet.getDouble("Quantity"),
                    resultSet.getString("UnitsMeasured")
            );
            itemDtos.add(dto);
        }

        resultSet.close();
        return itemDtos;
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT InventoryItemID FROM InventoryItems ORDER BY InventoryItemID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("I%03d", newIdIndex);
        }
        return "I001";
    }

    public String searchInventoryItemName(String itemName) throws ClassNotFoundException, SQLException {
        String sql = "SELECT InventoryItemID FROM InventoryItems WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + itemName.toLowerCase() + "%");

        String itemID = null;

        while (resultSet.next()) {
            itemID = resultSet.getString("InventoryItemID");

        }

        resultSet.close();
        return itemID;
    }

    public String getItemUnits(String itemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT UnitsMeasured FROM InventoryItems WHERE InventoryItemID LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + itemID + "%");

        String unit = null;

        while (resultSet.next()) {
            unit = resultSet.getString("UnitsMeasured");

        }

        resultSet.close();
        return unit;
    }

    public String getItemUnitsByName(String itemName) throws ClassNotFoundException, SQLException {
        String sql = "SELECT UnitsMeasured FROM InventoryItems WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + itemName.toLowerCase() + "%");

        String unit = null;

        while (resultSet.next()) {
            unit = resultSet.getString("UnitsMeasured");

        }

        resultSet.close();
        return unit;
    }


}


