package org.gourmetDelight.bo.custom;

import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.inventory.InventoryItemDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface InventoryItemsBO extends SuperBO {

    public ArrayList<InventoryItemDto> getAll() throws SQLException, ClassNotFoundException;


    public boolean save(InventoryItemDto item) throws SQLException, ClassNotFoundException;


    public boolean update(InventoryItemDto item) throws SQLException, ClassNotFoundException;


    public boolean delete(String itemId) throws SQLException, ClassNotFoundException;

    public InventoryItemDto searchById(String itemId) throws ClassNotFoundException, SQLException;

    public ArrayList<InventoryItemDto> searchByName(String name) throws ClassNotFoundException, SQLException;


    public String suggestNextID() throws ClassNotFoundException, SQLException;

    public String searchInventoryItemName(String itemName) throws ClassNotFoundException, SQLException;

    public String getItemUnits(String itemID) throws ClassNotFoundException, SQLException;

    public String getItemUnitsByName(String itemName) throws ClassNotFoundException, SQLException;

}
