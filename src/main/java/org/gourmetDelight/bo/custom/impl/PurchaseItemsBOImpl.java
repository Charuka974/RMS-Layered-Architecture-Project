package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.PurchaseItemsBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.PurchaseItemsDAO;
import org.gourmetDelight.dto.inventory.StockPurchaseItemsDto;
import org.gourmetDelight.entity.StockPurchaseItems;

import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseItemsBOImpl implements PurchaseItemsBO {

    PurchaseItemsDAO purchaseItemsDAO = (PurchaseItemsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PURCHASE_ITEMS);

    @Override
    public ArrayList<StockPurchaseItemsDto> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    public boolean save(StockPurchaseItemsDto purchaseItemsDto) throws SQLException, ClassNotFoundException {
        return purchaseItemsDAO.save(new StockPurchaseItems(
                purchaseItemsDto.getPurchaseID(),
                purchaseItemsDto.getInventoryItemID(),
                purchaseItemsDto.getUnitPerPrice(),
                purchaseItemsDto.getUnitPrice(),
                purchaseItemsDto.getUnitsBought(),
                purchaseItemsDto.getStatus()
        ));

    }

    public boolean delete(String purchaseID) throws ClassNotFoundException, SQLException {
       return purchaseItemsDAO.delete(purchaseID);
    }

    public boolean update(StockPurchaseItemsDto purchaseItemsDto) throws SQLException, ClassNotFoundException {
        return purchaseItemsDAO.update(new StockPurchaseItems(
                purchaseItemsDto.getPurchaseID(),
                purchaseItemsDto.getInventoryItemID(),
                purchaseItemsDto.getUnitPerPrice(),
                purchaseItemsDto.getUnitPrice(),
                purchaseItemsDto.getUnitsBought(),
                purchaseItemsDto.getStatus()
        ));
    }

    @Override
    public StockPurchaseItemsDto searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<StockPurchaseItemsDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }

}
