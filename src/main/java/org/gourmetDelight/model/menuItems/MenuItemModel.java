package org.gourmetDelight.model.menuItems;

import org.gourmetDelight.dto.menuItems.MenuItemDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;

public class MenuItemModel {

    public ArrayList<MenuItemDto> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT MenuItemID, Name, Description, Price, Category, KitchenSection FROM MenuItems";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<MenuItemDto> menuItems = new ArrayList<>();

        while (resultSet.next()) {
            String menuItemID = resultSet.getString("MenuItemID");
            String name = resultSet.getString("Name");
            String description = resultSet.getString("Description");
            double price = resultSet.getDouble("Price");
            String category = resultSet.getString("Category");
            String kitchenSection = resultSet.getString("KitchenSection");

            MenuItemDto menuItem = new MenuItemDto(menuItemID, name, description, price, category, kitchenSection);
            menuItems.add(menuItem);
        }

        resultSet.close();
        return menuItems;
    }

    public String saveMenuItem(MenuItemDto menuItemDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO MenuItems (MenuItemID, Name, Description, Price, Category, KitchenSection) VALUES (?, ?, ?, ?, ?, ?)";
        Boolean result = CrudUtil.execute(sql,
                menuItemDto.getMenuItemID(),
                menuItemDto.getName(),
                menuItemDto.getDescription(),
                menuItemDto.getPrice(),
                menuItemDto.getCategory(),
                menuItemDto.getKitchenSection()
        );
        return result ? "Successfully saved" : "Failed to save";
    }

    public String deleteMenuItem(String menuItemId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM MenuItems WHERE MenuItemID = ?";
        Boolean result = CrudUtil.execute(sql, menuItemId);
        return result ? "Successfully deleted" : "Failed to delete";
    }

    public String updateMenuItem(MenuItemDto menuItemDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE MenuItems SET Name = ?, Description = ?, Price = ?, Category = ?, KitchenSection = ? WHERE MenuItemID = ?";
        Boolean result = CrudUtil.execute(sql,
                menuItemDto.getName(),
                menuItemDto.getDescription(),
                menuItemDto.getPrice(),
                menuItemDto.getCategory(),
                menuItemDto.getKitchenSection(),
                menuItemDto.getMenuItemID()
        );
        return result ? "Successfully updated" : "Failed to update";
    }

    public MenuItemDto searchMenuItemById(String menuItemId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItems WHERE MenuItemID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, menuItemId);

        if (resultSet.next()) {
            MenuItemDto menuItem = new MenuItemDto(
                    resultSet.getString("MenuItemID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Description"),
                    resultSet.getDouble("Price"),
                    resultSet.getString("Category"),
                    resultSet.getString("KitchenSection")
            );
            resultSet.close();
            return menuItem;
        } else {
            resultSet.close();
            return null;
        }
    }

    public ArrayList<MenuItemDto> searchMenuItemsByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItems WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<MenuItemDto> menuItems = new ArrayList<>();

        while (resultSet.next()) {
            MenuItemDto dto = new MenuItemDto(
                    resultSet.getString("MenuItemID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Description"),
                    resultSet.getDouble("Price"),
                    resultSet.getString("Category"),
                    resultSet.getString("KitchenSection")
            );
            menuItems.add(dto);
        }

        resultSet.close();
        return menuItems;
    }
    

    public String suggestNextMenuItemID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT MenuItemID FROM MenuItems ORDER BY MenuItemID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("M%03d", newIdIndex);
        }
        return "M001";
    }
}
