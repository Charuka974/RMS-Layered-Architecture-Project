package org.gourmetDelight.model.inventory;

import org.gourmetDelight.dto.employee.EmployeeDto;
import org.gourmetDelight.dto.inventory.InventoryItemDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;

public class InventoryModel {

    public static ArrayList<InventoryItemDto> getAllItems() throws SQLException, ClassNotFoundException {

        String query = "SELECT InventoryItemID, Name, Description, Quantity, UnitsMeasured FROM InventoryItems";

        ResultSet resultSet = CrudUtil.execute(query);

        ArrayList<InventoryItemDto> itemList = new ArrayList<>();

        while (resultSet.next()) {

            String inventoryItemId = resultSet.getString("InventoryItemID");
            String name = resultSet.getString("Name");
            String description = resultSet.getString("Description");
            double quantity = resultSet.getDouble("Quantity");
            String unit = resultSet.getString("UnitsMeasured");

            InventoryItemDto item = new InventoryItemDto(inventoryItemId, name, description, quantity, unit);
            itemList.add(item);
        }

        resultSet.close();

        return itemList;
    }


    public String addItem(InventoryItemDto item) throws SQLException, ClassNotFoundException {

        String query = "INSERT INTO InventoryItems (InventoryItemID, Name, Description, Quantity, UnitsMeasured) VALUES (?, ?, ?, ?, ?)";

        Boolean result = CrudUtil.execute(query,
                item.getInventoryItemId(),
                item.getName(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnit()
        );
        return result ? "Successfully saved" : "Failed to save";

    }


    public String updateItem(InventoryItemDto item) throws SQLException, ClassNotFoundException {

        String query = "UPDATE InventoryItems SET Name = ?, Description = ?, Quantity = ?, UnitsMeasured = ? WHERE InventoryItemID = ?";

        Boolean result = CrudUtil.execute(query,
                item.getName(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnit(),
                item.getInventoryItemId()
        );
        return result ? "Successfully updated" : "Failed to update";

    }


    public String deleteItem(String itemId) throws SQLException, ClassNotFoundException {

        String query = "DELETE FROM InventoryItems WHERE InventoryItemID = ?";

        Boolean result = CrudUtil.execute(query, itemId);
        return result ? "Successfully deleted" : "Failed to delete";
    }

    public InventoryItemDto searchItemById(String itemId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM InventoryItems WHERE InventoryItemID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, itemId);

        if (resultSet.next()) {
            InventoryItemDto item = new InventoryItemDto(
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

    public ArrayList<InventoryItemDto> searchItemByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM InventoryItems WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<InventoryItemDto> itemDtos = new ArrayList<>();

        while (resultSet.next()) {
            InventoryItemDto dto = new InventoryItemDto(
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


    public String suggestNextItemID() throws ClassNotFoundException, SQLException {
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

}


