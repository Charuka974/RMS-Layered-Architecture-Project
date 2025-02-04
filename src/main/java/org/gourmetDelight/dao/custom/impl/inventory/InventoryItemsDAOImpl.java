package org.gourmetDelight.dao.custom.impl.inventory;

import javafx.scene.control.Alert;
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

        return CrudUtil.execute(query, itemId);
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

    public boolean updateInventory(String inventoryItemID, double unitsReceived) throws SQLException, ClassNotFoundException {
        try {
            // Check current inventory quantity
            String checkInventorySQL = "SELECT Quantity FROM InventoryItems WHERE InventoryItemID = ?";
            ResultSet inventoryResult = CrudUtil.execute(checkInventorySQL, inventoryItemID);

            if (inventoryResult.next()) {
                double currentQuantity = inventoryResult.getDouble("Quantity");
                double newQuantity = currentQuantity + unitsReceived;

                // Update inventory with the new quantity
                String updateInventorySQL = "UPDATE InventoryItems SET Quantity = ? WHERE InventoryItemID = ?";
                boolean isUpdated = CrudUtil.execute(updateInventorySQL, newQuantity, inventoryItemID);

                if (!isUpdated) {
                    throw new SQLException("Failed to update inventory for item: " + inventoryItemID);
                }
            } else {
                throw new SQLException("Inventory item not found: " + inventoryItemID);
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error during inventory update: " + e.getMessage());
            throw e;
        }
    }


    // Update inventory quantity for the given item in Purchase
    public boolean updateInventoryQuantityForPurchase(String itemID, double unitsBought) throws SQLException, ClassNotFoundException {
        String updateInventorySql = "UPDATE InventoryItems SET Quantity = Quantity - ? WHERE InventoryItemID = ?";
        return CrudUtil.execute(updateInventorySql, unitsBought, itemID);
    }

    public boolean decreaseFromInventoryForOrder(String menuItemID, String itemQuantity) throws SQLException, ClassNotFoundException {
        double orderQuantity = Double.parseDouble(itemQuantity);

        try {
            // Retrieve the required ingredients for the selected menu item
            String getIngredientsSQL = "SELECT InventoryItemID, QuantityNeeded FROM MenuItemIngredients WHERE MenuItemID = ?";
            ResultSet ingredientsResult = CrudUtil.execute(getIngredientsSQL, menuItemID);

            // Loop through each ingredient and check inventory availability
            while (ingredientsResult.next()) {
                String inventoryItemID = ingredientsResult.getString("InventoryItemID");
                double quantityNeededPerItem = ingredientsResult.getDouble("QuantityNeeded");
                double totalQuantityNeeded = quantityNeededPerItem * orderQuantity;

                // Check if there is enough inventory
                String checkInventorySQL = "SELECT Quantity FROM InventoryItems WHERE InventoryItemID = ?";
                ResultSet inventoryResult = CrudUtil.execute(checkInventorySQL, inventoryItemID);

                if (inventoryResult.next()) {
                    double currentQuantity = inventoryResult.getDouble("Quantity");

                    if (currentQuantity < totalQuantityNeeded) {
                        showAlert(Alert.AlertType.INFORMATION, "Info", "Insufficient inventory", "Insufficient inventory for item: " + inventoryItemID);
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Order Failed", "Inventory Item not found" );
                }

                // Update the inventory by deducting the total quantity needed
                String updateInventorySQL = "UPDATE InventoryItems SET Quantity = Quantity - ? WHERE InventoryItemID = ?";
                boolean isUpdated = CrudUtil.execute(updateInventorySQL, totalQuantityNeeded, inventoryItemID);

                if (!isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Order Failed", "Failed to place Order" );
                }
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error during inventory update: " + e.getMessage());
            throw e;
        }


    }

    public boolean increaseInventoryForOrder(String menuItemID, double quantityOfOrder) throws SQLException, ClassNotFoundException {

        try {

            String getIngredientsSQL = "SELECT InventoryItemID, QuantityNeeded FROM MenuItemIngredients WHERE MenuItemID = ?";
            ResultSet ingredientsResult = CrudUtil.execute(getIngredientsSQL, menuItemID);


            while (ingredientsResult.next()) {
                String inventoryItemID = ingredientsResult.getString("InventoryItemID");
                double quantityNeededPerItem = ingredientsResult.getDouble("QuantityNeeded");
                double totalQuantityNeeded = quantityNeededPerItem * quantityOfOrder;


                String updateInventorySQL = "UPDATE InventoryItems SET Quantity = Quantity + ? WHERE InventoryItemID = ?";
                boolean isUpdated = CrudUtil.execute(updateInventorySQL, totalQuantityNeeded, inventoryItemID);

                if (!isUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Order Failed", "Failed to place Order" );
                }
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error during inventory update: " + e.getMessage());
            throw e;
        }


    }



    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType); // Use the provided alertType
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}


