package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.MenuItemIngredients;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MenuItemIngredientsDAO extends CrudDAO<MenuItemIngredients> {
    public ArrayList<MenuItemIngredients> getAll(String menuItemID) throws ClassNotFoundException, SQLException;

    public String delete(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException;

    public MenuItemIngredients searchIngredientItem(String menuItemID, String inventoryItemID) throws ClassNotFoundException, SQLException;

    public ArrayList<MenuItemIngredients> searchIngredientsByInventoryItemID(String inventoryItemID) throws ClassNotFoundException, SQLException;


}
