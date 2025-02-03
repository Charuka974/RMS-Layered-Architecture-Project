package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.MenuItemIngredientsBO;
import org.gourmetDelight.dao.custom.MenuItemIngredientsDAO;
import org.gourmetDelight.dao.custom.impl.menuItems.MenuItemIngredientsDAOImpl;
import org.gourmetDelight.dto.menuItems.MenuItemIngredientsDto;
import org.gourmetDelight.entity.MenuItemIngredients;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuItemIngredientsBOImpl implements MenuItemIngredientsBO {
    MenuItemIngredientsDAO  menuItemIngredientsDAO = new MenuItemIngredientsDAOImpl();

    public ArrayList<MenuItemIngredientsDto> getAll(String menuItemID) throws ClassNotFoundException, SQLException {
        ArrayList<MenuItemIngredients> menuItemIngredients = menuItemIngredientsDAO.getAll(menuItemID);
        ArrayList<MenuItemIngredientsDto> menuItemIngredientDTOs = new ArrayList<>();
        for (MenuItemIngredients menuItemIngredient : menuItemIngredients) {
            MenuItemIngredientsDto dto = new MenuItemIngredientsDto();
            dto.setMenuItemID(menuItemIngredient.getMenuItemID());
            dto.setInventoryItemID(menuItemIngredient.getInventoryItemID());
            dto.setQuantityNeeded(menuItemIngredient.getQuantityNeeded());
            menuItemIngredientDTOs.add(dto);
        }
        return menuItemIngredientDTOs;
    }

    @Override
    public ArrayList<MenuItemIngredientsDto> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    // Save a new ingredient item
    public boolean save(MenuItemIngredientsDto dto) throws ClassNotFoundException, SQLException {
       return menuItemIngredientsDAO.save(new MenuItemIngredients(dto.getMenuItemID(), dto.getInventoryItemID(), dto.getQuantityNeeded()));
    }

    @Override
    public boolean delete(String Id) throws ClassNotFoundException, SQLException {
        return false;
    }

    // Delete an ingredient item by MenuItemID and InventoryItemID
    public String delete(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException {
        return menuItemIngredientsDAO.delete(menuItemID, inventoryItemID);
    }

    // Update an existing ingredient item
    public boolean update(MenuItemIngredientsDto dto) throws ClassNotFoundException, SQLException {
        return menuItemIngredientsDAO.update(new MenuItemIngredients(dto.getMenuItemID(), dto.getInventoryItemID(), dto.getQuantityNeeded()));
    }

    @Override
    public MenuItemIngredientsDto searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<MenuItemIngredientsDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }

    public MenuItemIngredientsDto searchIngredientItem(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException {
        MenuItemIngredients itemIngredients = menuItemIngredientsDAO.searchIngredientItem(menuItemID, inventoryItemID);
        MenuItemIngredientsDto dto = new MenuItemIngredientsDto();
        dto.setMenuItemID(itemIngredients.getMenuItemID());
        dto.setInventoryItemID(itemIngredients.getInventoryItemID());
        dto.setQuantityNeeded(itemIngredients.getQuantityNeeded());

        return dto;
    }

    public ArrayList<MenuItemIngredientsDto> searchIngredientsByInventoryItemID(String inventoryItemID) throws ClassNotFoundException, SQLException {
        ArrayList<MenuItemIngredients> menuItemIngredients = menuItemIngredientsDAO.getAll(inventoryItemID);
        ArrayList<MenuItemIngredientsDto> menuItemIngredientDTOs = new ArrayList<>();
        for (MenuItemIngredients menuItemIngredient : menuItemIngredients) {
            MenuItemIngredientsDto dto = new MenuItemIngredientsDto();
            dto.setMenuItemID(menuItemIngredient.getMenuItemID());
            dto.setInventoryItemID(menuItemIngredient.getInventoryItemID());
            dto.setQuantityNeeded(menuItemIngredient.getQuantityNeeded());
            menuItemIngredientDTOs.add(dto);
        }
        return menuItemIngredientDTOs;
    }

}
