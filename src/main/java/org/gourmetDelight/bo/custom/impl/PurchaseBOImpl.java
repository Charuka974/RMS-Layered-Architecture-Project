package org.gourmetDelight.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.gourmetDelight.bo.custom.PurchaseBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.*;
import org.gourmetDelight.dao.custom.impl.QueryDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.InventoryItemsDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.PurchaseDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.PurchaseItemsDAOImpl;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.dto.custom.StockPurchaseDTOCustom;
import org.gourmetDelight.entity.Customer;
import org.gourmetDelight.entity.StockPurchase;
import org.gourmetDelight.entity.StockPurchaseItems;
import org.gourmetDelight.entity.custom.StockPurchaseCustom;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PurchaseBOImpl implements PurchaseBO {

    PurchaseDAO purchaseDAO = (PurchaseDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.STOCK_PURCHASE);
    PurchaseItemsDAO purchaseItemsDAO = (PurchaseItemsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PURCHASE_ITEMS);
    InventoryItemsDAO  inventoryItemsDAO = (InventoryItemsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.INVENTORY);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);


    @Override
    public ArrayList<StockPurchase> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(StockPurchase dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean delete(String Id) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean update(StockPurchase dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public StockPurchase searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    public StockPurchaseDTOCustom searchByIdReturnTM(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<StockPurchase> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }


    /*
      Saves a stock purchase and its items, updates inventory if the purchase status is "Received".
     */
    public boolean save(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false); // Start transaction

            // Save purchase details
            if (!purchaseDAO.save(purchaseDto)) {
                connection.rollback();
                return false;
            }

            // Save purchase items
            if (!purchaseItemsDAO.save(purchaseItemsDto)) {
                connection.rollback();
                return false;
            }

            // Update inventory if status is "Received"
            if ("Received".equalsIgnoreCase(purchaseItemsDto.getStatus())) {
                boolean inventoryUpdated = inventoryItemsDAO.updateInventory(
                        purchaseItemsDto.getInventoryItemID(), purchaseItemsDto.getUnitsBought()
                );
                if (!inventoryUpdated) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Error during stock purchase save: " + e.getMessage());
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }



    public boolean updateStockPurchase(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);


            if (!purchaseDAO.update(purchaseDto)) {
                connection.rollback();
                return false;
            }
            ResultSet itemResultSet = purchaseItemsDAO.fetchItemFromBothIds(purchaseItemsDto);
            if (itemResultSet.next()) {
                String currentStatus = itemResultSet.getString("Status");
                double currentUnitsBought = itemResultSet.getDouble("UnitsBought");

                if (!currentStatus.equalsIgnoreCase(purchaseItemsDto.getStatus())) {
                    if ("Received".equalsIgnoreCase(currentStatus)) {

                        if (!inventoryItemsDAO.updateInventory(purchaseItemsDto.getInventoryItemID(), -currentUnitsBought)) {
                            connection.rollback();
                            return false;
                        }
                    }
                    if ("Received".equalsIgnoreCase(purchaseItemsDto.getStatus())) {
                        if (!inventoryItemsDAO.updateInventory(purchaseItemsDto.getInventoryItemID(), purchaseItemsDto.getUnitsBought())) {
                            connection.rollback();
                            return false;
                        }
                    }
                } else if ("Received".equalsIgnoreCase(currentStatus)) {
                    double unitsDifference = purchaseItemsDto.getUnitsBought() - currentUnitsBought;
                    if (!inventoryItemsDAO.updateInventory(purchaseItemsDto.getInventoryItemID(), unitsDifference)) {
                        connection.rollback();
                        return false;
                    }
                }

                if (!purchaseItemsDAO.update(purchaseItemsDto)) {
                    connection.rollback();
                    return false;
                }
            } else {
                connection.rollback();
                throw new SQLException("Item not found: " + purchaseItemsDto.getInventoryItemID());
            }



            connection.commit();
            return true;

        } catch (SQLException e) {

            connection.rollback();
            System.err.println("Error during stock purchase update: " + e.getMessage());
            return false;
        } finally {

            connection.setAutoCommit(true);
        }
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
       return purchaseDAO.suggestNextID();
    }


    public String deletePurchase(String purchaseID) throws ClassNotFoundException, SQLException {
        Connection connection = null; // Declare connection outside try to ensure access in catch/finally
        try {
            // Get the database connection
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Fetch items from PurchaseItems table
            ArrayList<StockPurchaseItems> purchaseItems = purchaseItemsDAO.fetchPurchaseItems(purchaseID);

            // Process each item to adjust inventory quantities if the status is "Received"
            for (StockPurchaseItems item : purchaseItems) {
                if ("Received".equalsIgnoreCase(item.getStatus())) {
                    boolean updateInventoryResult = inventoryItemsDAO.updateInventoryQuantityForPurchase(item.getInventoryItemID(), item.getUnitsBought());

                    if (!updateInventoryResult) {
                        connection.rollback(); // Rollback transaction if update fails
                        return "Failed to update inventory for item: " + item.getInventoryItemID();
                    }
                }
            }

            // Delete from PurchaseItems table
            boolean deleteItemsResult = purchaseItemsDAO.delete(purchaseID);
            if (!deleteItemsResult) {
                connection.rollback(); // Rollback transaction if delete fails
                return "Failed to delete purchase items.";
            }

            // Delete from Purchases table
            boolean deletePurchaseResult = purchaseDAO.delete(purchaseID);
            if (!deletePurchaseResult) {
                connection.rollback(); // Rollback transaction if delete fails
                return "Failed to delete purchase.";
            }

            // Commit the transaction if both delete operations are successful
            connection.commit();
            return "Successfully deleted purchase.";

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx); // Add the rollback exception as a suppressed exception to the original exception
                }
            }
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        } finally {
            // Reset auto-commit mode to true
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle exception if setting auto-commit fails
                }
            }
        }
    }





    public ObservableList<StockPurchaseDTOCustom> getAllStockPurchases() throws ClassNotFoundException, SQLException {
        ArrayList<StockPurchaseCustom> purchases = queryDAO.getAllStockPurchases();
        ArrayList<StockPurchaseDTOCustom> purchaseDtos = new ArrayList<>();
        for (StockPurchaseCustom purchase : purchases) {
            StockPurchaseDTOCustom dto = new StockPurchaseDTOCustom();
            dto.setPurchaseID(purchase.getPurchaseID());
            dto.setItemID(purchase.getItemID());
            dto.setItemName(purchase.getItemName());
            dto.setUnitPrice(purchase.getUnitPrice());
            dto.setQuantity(purchase.getQuantity());
            dto.setTotalAmount(purchase.getTotalAmount());
            dto.setPurchaseDate(purchase.getPurchaseDate());
            dto.setSupplierID(purchase.getSupplierID());
            dto.setStatus(purchase.getStatus());
            dto.setSupplierName(purchase.getSupplierName());
            dto.setUnit(purchase.getUnit());
            dto.setUnitsMeasured(purchase.getUnitsMeasured());
            purchaseDtos.add(dto);
        }
        return FXCollections.observableArrayList(purchaseDtos);
    }




}
