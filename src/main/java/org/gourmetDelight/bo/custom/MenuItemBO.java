package org.gourmetDelight.bo.custom;

import org.gourmetDelight.dto.menuItems.MenuItemDto;
import org.gourmetDelight.entity.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MenuItemBO {

    public ArrayList<MenuItemDto> getAll() throws ClassNotFoundException, SQLException;

    public boolean save(MenuItemDto menuItemDto) throws ClassNotFoundException, SQLException;

    public boolean delete(String menuItemId) throws ClassNotFoundException, SQLException;

    public boolean update(MenuItemDto menuItemDto) throws ClassNotFoundException, SQLException;

    public MenuItemDto searchById(String menuItemId) throws ClassNotFoundException, SQLException;

    public ArrayList<MenuItemDto> searchByName(String name) throws ClassNotFoundException, SQLException;

    public String suggestNextID() throws ClassNotFoundException, SQLException;

}
