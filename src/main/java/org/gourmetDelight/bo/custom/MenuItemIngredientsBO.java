package org.gourmetDelight.bo.custom;

import org.gourmetDelight.dto.menuItems.MenuItemIngredientsDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MenuItemIngredientsBO {

    public ArrayList<MenuItemIngredientsDto> getAll(String menuItemID) throws ClassNotFoundException, SQLException;
    public ArrayList<MenuItemIngredientsDto> getAll() throws ClassNotFoundException, SQLException;
    public boolean save(MenuItemIngredientsDto menuItemIngredientsDto) throws ClassNotFoundException, SQLException;
    public boolean delete(String Id) throws ClassNotFoundException, SQLException;

    public String delete(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException;


    public boolean update(MenuItemIngredientsDto menuItemIngredientsDto) throws ClassNotFoundException, SQLException;
    public MenuItemIngredientsDto searchById(String Id) throws ClassNotFoundException, SQLException;
    public ArrayList<MenuItemIngredientsDto> searchByName(String name) throws ClassNotFoundException, SQLException;
    public String suggestNextID() throws ClassNotFoundException, SQLException;

    public MenuItemIngredientsDto searchIngredientItem(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException;


    public ArrayList<MenuItemIngredientsDto> searchIngredientsByInventoryItemID(String inventoryItemID) throws ClassNotFoundException, SQLException;

}
