package org.gourmetDelight.dao.custom.impl.menuItems;

import org.gourmetDelight.dao.custom.MenuItemIngredientsDAO;
import org.gourmetDelight.dto.menuItems.MenuItemIngredientsDto;
import org.gourmetDelight.entity.MenuItemIngredients;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItemIngredientsDAOImpl implements MenuItemIngredientsDAO {

    // Retrieve all ingredient items for a specific menu item
    public ArrayList<MenuItemIngredients> getAll(String menuItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE MenuItemID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, menuItemID);

        ArrayList<MenuItemIngredients> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            String menuID = resultSet.getString("MenuItemID");
            String inventoryID = resultSet.getString("InventoryItemID");
            double quantityNeeded = resultSet.getDouble("QuantityNeeded");

            MenuItemIngredients ingredient = new MenuItemIngredients(menuID, inventoryID, quantityNeeded);
            ingredients.add(ingredient);
        }

        resultSet.close();
        return ingredients;
    }

    @Override
    public ArrayList<MenuItemIngredients> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    // Save a new ingredient item
    public boolean save(MenuItemIngredients menuItemIngredientsDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO MenuItemIngredients (MenuItemID, InventoryItemID, QuantityNeeded) VALUES (?, ?, ?)";
        boolean result = SQLUtil.execute(sql,
                menuItemIngredientsDto.getMenuItemID(),
                menuItemIngredientsDto.getInventoryItemID(),
                menuItemIngredientsDto.getQuantityNeeded()
        );
        return result;
    }

    @Override
    public boolean delete(String Id) throws ClassNotFoundException, SQLException {
        return false;
    }

    // Delete an ingredient item by MenuItemID and InventoryItemID
    public String delete(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM MenuItemIngredients WHERE MenuItemID = ? AND InventoryItemID = ?";
        boolean result = SQLUtil.execute(sql, menuItemID, inventoryItemID);
        return result ? "Successfully deleted" : "Failed to delete";
    }

    // Update an existing ingredient item
    public boolean update(MenuItemIngredients menuItemIngredientsDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE MenuItemIngredients SET QuantityNeeded = ? WHERE MenuItemID = ? AND InventoryItemID = ?";
        boolean result = SQLUtil.execute(sql,
                menuItemIngredientsDto.getQuantityNeeded(),
                menuItemIngredientsDto.getMenuItemID(),
                menuItemIngredientsDto.getInventoryItemID()
        );
        return result;
    }

    @Override
    public MenuItemIngredients searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<MenuItemIngredients> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }

    // Search for an ingredient item by MenuItemID and InventoryItemID
    public MenuItemIngredients searchIngredientItem(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE MenuItemID = ? AND InventoryItemID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, menuItemID, inventoryItemID);

        if (resultSet.next()) {
            String menuID = resultSet.getString("MenuItemID");
            String inventoryID = resultSet.getString("InventoryItemID");
            double quantityNeeded = resultSet.getDouble("QuantityNeeded");

            resultSet.close();
            return new MenuItemIngredients(menuID, inventoryID, quantityNeeded);
        } else {
            resultSet.close();
            return null;
        }
    }

    // Retrieve all ingredient items containing a specific inventory item ID
    public ArrayList<MenuItemIngredients> searchIngredientsByInventoryItemID(String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE InventoryItemID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, inventoryItemID);

        ArrayList<MenuItemIngredients> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            String menuID = resultSet.getString("MenuItemID");
            String inventoryID = resultSet.getString("InventoryItemID");
            double quantityNeeded = resultSet.getDouble("QuantityNeeded");

            MenuItemIngredients ingredient = new MenuItemIngredients(menuID, inventoryID, quantityNeeded);
            ingredients.add(ingredient);
        }

        resultSet.close();
        return ingredients;
    }

    public ArrayList<MenuItemIngredientsDto> searchIngredientsByInventoryItemID2(String inventoryItemID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItemIngredients WHERE InventoryItemID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, inventoryItemID);

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


}
