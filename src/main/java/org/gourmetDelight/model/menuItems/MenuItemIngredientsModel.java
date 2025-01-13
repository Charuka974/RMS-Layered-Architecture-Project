package org.gourmetDelight.model.menuItems;

import org.gourmetDelight.dto.menuItems.MenuItemIngredientsDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItemIngredientsModel {

    // Retrieve all ingredient items for a specific menu item
    public static ArrayList<MenuItemIngredientsDto> getAllIngredients(String menuItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE MenuItemID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, menuItemID);

        ArrayList<MenuItemIngredientsDto> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            String menuID = resultSet.getString("MenuItemID");
            String inventoryID = resultSet.getString("InventoryItemID");
            double quantityNeeded = resultSet.getDouble("QuantityNeeded");

            MenuItemIngredientsDto ingredient = new MenuItemIngredientsDto(menuID, inventoryID, quantityNeeded);
            ingredients.add(ingredient);
        }

        resultSet.close();
        return ingredients;
    }

    // Save a new ingredient item
    public String saveIngredientItem(MenuItemIngredientsDto menuItemIngredientsDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO MenuItemIngredients (MenuItemID, InventoryItemID, QuantityNeeded) VALUES (?, ?, ?)";
        boolean result = CrudUtil.execute(sql,
                menuItemIngredientsDto.getMenuItemID(),
                menuItemIngredientsDto.getInventoryItemID(),
                menuItemIngredientsDto.getQuantityNeeded()
        );
        return result ? "Successfully saved" : "Failed to save";
    }

    // Delete an ingredient item by MenuItemID and InventoryItemID
    public String deleteIngredientItem(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM MenuItemIngredients WHERE MenuItemID = ? AND InventoryItemID = ?";
        boolean result = CrudUtil.execute(sql, menuItemID, inventoryItemID);
        return result ? "Successfully deleted" : "Failed to delete";
    }

    // Update an existing ingredient item
    public String updateIngredientItem(MenuItemIngredientsDto menuItemIngredientsDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE MenuItemIngredients SET QuantityNeeded = ? WHERE MenuItemID = ? AND InventoryItemID = ?";
        boolean result = CrudUtil.execute(sql,
                menuItemIngredientsDto.getQuantityNeeded(),
                menuItemIngredientsDto.getMenuItemID(),
                menuItemIngredientsDto.getInventoryItemID()
        );
        return result ? "Successfully updated" : "Failed to update";
    }

    // Search for an ingredient item by MenuItemID and InventoryItemID
    public MenuItemIngredientsDto searchIngredientItem(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE MenuItemID = ? AND InventoryItemID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, menuItemID, inventoryItemID);

        if (resultSet.next()) {
            String menuID = resultSet.getString("MenuItemID");
            String inventoryID = resultSet.getString("InventoryItemID");
            double quantityNeeded = resultSet.getDouble("QuantityNeeded");

            resultSet.close();
            return new MenuItemIngredientsDto(menuID, inventoryID, quantityNeeded);
        } else {
            resultSet.close();
            return null;
        }
    }

    // Retrieve all ingredient items containing a specific inventory item ID
    public ArrayList<MenuItemIngredientsDto> searchIngredientsByInventoryItemID(String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE InventoryItemID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, inventoryItemID);

        ArrayList<MenuItemIngredientsDto> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            String menuID = resultSet.getString("MenuItemID");
            String inventoryID = resultSet.getString("InventoryItemID");
            double quantityNeeded = resultSet.getDouble("QuantityNeeded");

            MenuItemIngredientsDto ingredient = new MenuItemIngredientsDto(menuID, inventoryID, quantityNeeded);
            ingredients.add(ingredient);
        }

        resultSet.close();
        return ingredients;
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
