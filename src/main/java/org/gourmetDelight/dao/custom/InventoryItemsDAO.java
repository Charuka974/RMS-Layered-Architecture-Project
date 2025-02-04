package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.InventoryItem;

import java.sql.SQLException;

public interface InventoryItemsDAO extends CrudDAO<InventoryItem> {

    public String searchInventoryItemName(String itemName) throws ClassNotFoundException, SQLException;

    public String getItemUnits(String itemID) throws ClassNotFoundException, SQLException;

    public String getItemUnitsByName(String itemName) throws ClassNotFoundException, SQLException;

    public boolean updateInventory(String inventoryItemID, double unitsReceived) throws SQLException, ClassNotFoundException;

    public boolean updateInventoryQuantityForPurchase(String itemID, double unitsBought) throws SQLException, ClassNotFoundException;

    public boolean decreaseFromInventoryForOrder(String menuItemID, String itemQuantity) throws SQLException, ClassNotFoundException;

    public boolean increaseInventoryForOrder(String menuItemID, double quantityOfOrder) throws SQLException, ClassNotFoundException;


    }


