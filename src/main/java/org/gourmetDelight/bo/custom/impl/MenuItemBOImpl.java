package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.MenuItemBO;
import org.gourmetDelight.dao.custom.MenuItemDAO;
import org.gourmetDelight.dao.custom.impl.menuItems.MenuItemDAOImpl;
import org.gourmetDelight.dto.menuItems.MenuItemDto;
import org.gourmetDelight.entity.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItemBOImpl implements MenuItemBO {
    MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    public ArrayList<MenuItemDto> getAll() throws ClassNotFoundException, SQLException {
        ArrayList<MenuItem> menuItems = menuItemDAO.getAll();
        ArrayList<MenuItemDto> menuItemDTOs = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            MenuItemDto dto = new MenuItemDto();
            dto.setMenuItemID(menuItem.getMenuItemID());
            dto.setName(menuItem.getName());
            dto.setDescription(menuItem.getDescription());
            dto.setPrice(menuItem.getPrice());
            dto.setCategory(menuItem.getCategory());
            dto.setKitchenSection(menuItem.getKitchenSection());
            menuItemDTOs.add(dto);
        }
        return menuItemDTOs;
    }

    public boolean save(MenuItemDto dto) throws ClassNotFoundException, SQLException {
        return menuItemDAO.save(new MenuItem(dto.getMenuItemID(), dto.getName(), dto.getDescription(), dto.getPrice(), dto.getCategory(), dto.getKitchenSection()));

    }

    public boolean delete(String menuItemId) throws ClassNotFoundException, SQLException {
        return menuItemDAO.delete(menuItemId);
    }

    public boolean update(MenuItemDto dto) throws ClassNotFoundException, SQLException {
        return menuItemDAO.save(new MenuItem(dto.getMenuItemID(), dto.getName(), dto.getDescription(), dto.getPrice(), dto.getCategory(), dto.getKitchenSection()));

    }

    public MenuItemDto searchById(String menuItemId) throws ClassNotFoundException, SQLException {
        MenuItem menuItem = menuItemDAO.searchById(menuItemId);
        MenuItemDto dto = new MenuItemDto();
        dto.setMenuItemID(menuItem.getMenuItemID());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory());
        dto.setKitchenSection(menuItem.getKitchenSection());

        return dto;
    }

    public ArrayList<MenuItemDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        ArrayList<MenuItem> menuItems = menuItemDAO.searchByName(name);
        ArrayList<MenuItemDto> menuItemDTOs = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            MenuItemDto dto = new MenuItemDto();
            dto.setMenuItemID(menuItem.getMenuItemID());
            dto.setName(menuItem.getName());
            dto.setDescription(menuItem.getDescription());
            dto.setPrice(menuItem.getPrice());
            dto.setCategory(menuItem.getCategory());
            dto.setKitchenSection(menuItem.getKitchenSection());
            menuItemDTOs.add(dto);
        }
        return menuItemDTOs;
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return menuItemDAO.suggestNextID();
    }

}
