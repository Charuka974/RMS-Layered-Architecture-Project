package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.InventoryItemsBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.CustomerDAO;
import org.gourmetDelight.dao.custom.InventoryItemsDAO;
import org.gourmetDelight.dao.custom.impl.inventory.InventoryItemsDAOImpl;
import org.gourmetDelight.dto.inventory.InventoryItemDto;
import org.gourmetDelight.entity.InventoryItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryItemsBOImpl implements InventoryItemsBO {

    InventoryItemsDAO inventoryItemsBO = (InventoryItemsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.INVENTORY);

    public ArrayList<InventoryItemDto> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<InventoryItem> inventoryItems = inventoryItemsBO.getAll();
        ArrayList<InventoryItemDto> itemDTOs = new ArrayList<>();
        for (InventoryItem inventoryItem : inventoryItems) {
            InventoryItemDto dto = new InventoryItemDto();
            dto.setInventoryItemId(inventoryItem.getInventoryItemId());
            dto.setName(inventoryItem.getName());
            dto.setDescription(inventoryItem.getDescription());
            dto.setQuantity(inventoryItem.getQuantity());
            dto.setUnit(inventoryItem.getUnit());
            itemDTOs.add(dto);
        }
        return itemDTOs;
    }


    public boolean save(InventoryItemDto item) throws SQLException, ClassNotFoundException {

      return inventoryItemsBO.save(new InventoryItem(item.getInventoryItemId(), item.getName(), item.getDescription(), item.getQuantity(), item.getUnit()));

    }


    public boolean update(InventoryItemDto item) throws SQLException, ClassNotFoundException {

       return inventoryItemsBO.update(new InventoryItem(item.getInventoryItemId(), item.getName(), item.getDescription(), item.getQuantity(), item.getUnit()));

    }


    public boolean delete(String itemId) throws SQLException, ClassNotFoundException {
        return inventoryItemsBO.delete(itemId);
    }

    public InventoryItemDto searchById(String itemId) throws ClassNotFoundException, SQLException {
        InventoryItem inventoryItem = inventoryItemsBO.searchById(itemId);
        InventoryItemDto dto = new InventoryItemDto();
        dto.setInventoryItemId(inventoryItem.getInventoryItemId());
        dto.setName(inventoryItem.getName());
        dto.setDescription(inventoryItem.getDescription());
        dto.setQuantity(inventoryItem.getQuantity());
        dto.setUnit(inventoryItem.getUnit());

        return dto;
    }

    public ArrayList<InventoryItemDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        ArrayList<InventoryItem> inventoryItems = inventoryItemsBO.searchByName(name);
        ArrayList<InventoryItemDto> itemDTOs = new ArrayList<>();
        for (InventoryItem inventoryItem : inventoryItems) {
            InventoryItemDto dto = new InventoryItemDto();
            dto.setInventoryItemId(inventoryItem.getInventoryItemId());
            dto.setName(inventoryItem.getName());
            dto.setDescription(inventoryItem.getDescription());
            dto.setQuantity(inventoryItem.getQuantity());
            dto.setUnit(inventoryItem.getUnit());
            itemDTOs.add(dto);
        }
        return itemDTOs;
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
       return inventoryItemsBO.suggestNextID();
    }

    public String searchInventoryItemName(String itemName) throws ClassNotFoundException, SQLException {
       return inventoryItemsBO.searchInventoryItemName(itemName);
    }

    public String getItemUnits(String itemID) throws ClassNotFoundException, SQLException {
       return inventoryItemsBO.getItemUnits(itemID);
    }

    public String getItemUnitsByName(String itemName) throws ClassNotFoundException, SQLException {
        return inventoryItemsBO.getItemUnitsByName(itemName);
    }

}
