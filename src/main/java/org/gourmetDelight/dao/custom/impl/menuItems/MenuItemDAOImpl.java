package org.gourmetDelight.dao.custom.impl.menuItems;

import org.gourmetDelight.dao.custom.MenuItemDAO;
import org.gourmetDelight.entity.MenuItem;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItemDAOImpl implements MenuItemDAO {

    public ArrayList<MenuItem> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT MenuItemID, Name, Description, Price, Category, KitchenSection FROM MenuItems";
        ResultSet resultSet = SQLUtil.execute(sql);

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        while (resultSet.next()) {
            String menuItemID = resultSet.getString("MenuItemID");
            String name = resultSet.getString("Name");
            String description = resultSet.getString("Description");
            double price = resultSet.getDouble("Price");
            String category = resultSet.getString("Category");
            String kitchenSection = resultSet.getString("KitchenSection");

            MenuItem menuItem = new MenuItem(menuItemID, name, description, price, category, kitchenSection);
            menuItems.add(menuItem);
        }

        resultSet.close();
        return menuItems;
    }

    public boolean save(MenuItem menuItemDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO MenuItems (MenuItemID, Name, Description, Price, Category, KitchenSection) VALUES (?, ?, ?, ?, ?, ?)";
        Boolean result = SQLUtil.execute(sql,
                menuItemDto.getMenuItemID(),
                menuItemDto.getName(),
                menuItemDto.getDescription(),
                menuItemDto.getPrice(),
                menuItemDto.getCategory(),
                menuItemDto.getKitchenSection()
        );
        return result;
    }

    public boolean delete(String menuItemId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM MenuItems WHERE MenuItemID = ?";
        Boolean result = SQLUtil.execute(sql, menuItemId);
        return result;
    }

    public boolean update(MenuItem menuItemDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE MenuItems SET Name = ?, Description = ?, Price = ?, Category = ?, KitchenSection = ? WHERE MenuItemID = ?";
        Boolean result = SQLUtil.execute(sql,
                menuItemDto.getName(),
                menuItemDto.getDescription(),
                menuItemDto.getPrice(),
                menuItemDto.getCategory(),
                menuItemDto.getKitchenSection(),
                menuItemDto.getMenuItemID()
        );
        return result;
    }

    public MenuItem searchById(String menuItemId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItems WHERE MenuItemID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, menuItemId);

        if (resultSet.next()) {
            MenuItem menuItem = new MenuItem(
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

    public ArrayList<MenuItem> searchByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM MenuItems WHERE Name LIKE ?";
        ResultSet resultSet = SQLUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        while (resultSet.next()) {
            MenuItem dto = new MenuItem(
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
    

    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.execute("SELECT MenuItemID FROM MenuItems ORDER BY MenuItemID DESC LIMIT 1");

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
