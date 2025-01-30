package org.gourmetDelight.dao.custom.impl.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.gourmetDelight.dao.custom.PurchaseDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.StockPurchase;
import org.gourmetDelight.entity.StockPurchaseItems;
import org.gourmetDelight.dto.tm.StockPurchaseTM;
import org.gourmetDelight.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PurchaseDAOImpl implements PurchaseDAO {


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
            // Disable auto-commit for manual transaction control
            connection.setAutoCommit(false);

            // Step 1: Insert the stock purchase details
            String insertPurchaseSQL = "INSERT INTO Purchases (PurchaseID, SupplierID, TotalAmount, PurchaseDate) VALUES (?, ?, ?, ?)";
            boolean purchaseInserted = CrudUtil.execute(insertPurchaseSQL,
                    purchaseDto.getPurchaseID(),
                    purchaseDto.getSupplierID(),
                    purchaseDto.getTotalAmount(),
                    purchaseDto.getPurchaseDate());

            if (!purchaseInserted) {
                connection.rollback();
                return false; // Rollback if purchase insertion fails
            }

            // Step 2: Insert stock purchase items

                String insertPurchaseItemSQL = "INSERT INTO PurchaseItems (PurchaseID, InventoryItemID, Unit, UnitPrice, UnitsBought, Status) VALUES (?, ?, ?, ?, ?, ?)";
                boolean itemInserted = CrudUtil.execute(insertPurchaseItemSQL,
                        purchaseItemsDto.getPurchaseID(),
                        purchaseItemsDto.getInventoryItemID(),
                        purchaseItemsDto.getUnitPerPrice(),
                        purchaseItemsDto.getUnitPrice(),
                        purchaseItemsDto.getUnitsBought(),
                        purchaseItemsDto.getStatus());

                if (!itemInserted) {
                    connection.rollback();
                    return false; // Rollback if item insertion fails
                }

                // Step 3: Update inventory if the item status is "Received"
                if ("Received".equalsIgnoreCase(purchaseItemsDto.getStatus())) {
                    if (!updateInventory(purchaseItemsDto.getInventoryItemID(), purchaseItemsDto.getUnitsBought())) {
                        connection.rollback();
                        return false; // Rollback if inventory update fails
                    }
                }


            // Commit the transaction if all steps are successful
            connection.commit();
            return true;

        } catch (SQLException e) {
            // Rollback the transaction in case of any exception
            connection.rollback();
            System.err.println("Error during stock purchase save: " + e.getMessage());
            return false;
        } finally {
            // Reset auto-commit to true
            connection.setAutoCommit(true);
        }
    }

    /*
      Updates the inventory quantity for a specific item based on the units received.
     */
    public boolean updateInventory(String inventoryItemID, double unitsReceived) throws SQLException, ClassNotFoundException {
        try {
            // Check current inventory quantity
            String checkInventorySQL = "SELECT Quantity FROM InventoryItems WHERE InventoryItemID = ?";
            ResultSet inventoryResult = CrudUtil.execute(checkInventorySQL, inventoryItemID);

            if (inventoryResult.next()) {
                double currentQuantity = inventoryResult.getDouble("Quantity");
                double newQuantity = currentQuantity + unitsReceived;

                // Update inventory with the new quantity
                String updateInventorySQL = "UPDATE InventoryItems SET Quantity = ? WHERE InventoryItemID = ?";
                boolean isUpdated = CrudUtil.execute(updateInventorySQL, newQuantity, inventoryItemID);

                if (!isUpdated) {
                    throw new SQLException("Failed to update inventory for item: " + inventoryItemID);
                }
            } else {
                throw new SQLException("Inventory item not found: " + inventoryItemID);
            }

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error during inventory update: " + e.getMessage());
            throw e;
        }
    }


    public boolean updateStockPurchase(StockPurchase purchaseDto, StockPurchaseItems purchaseItemsDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            String updatePurchaseSQL = "UPDATE Purchases SET SupplierID = ?, TotalAmount = ?, PurchaseDate = ? WHERE PurchaseID = ?";
            boolean purchaseUpdated = CrudUtil.execute(updatePurchaseSQL,
                    purchaseDto.getSupplierID(),
                    purchaseDto.getTotalAmount(),
                    purchaseDto.getPurchaseDate(),
                    purchaseDto.getPurchaseID());

            if (!purchaseUpdated) {
                connection.rollback();
                return false;
            }


                String fetchItemSQL = "SELECT Status, UnitsBought FROM PurchaseItems WHERE PurchaseID = ? AND InventoryItemID = ?";
                ResultSet itemResultSet = CrudUtil.execute(fetchItemSQL, purchaseItemsDto.getPurchaseID(), purchaseItemsDto.getInventoryItemID());

                if (itemResultSet.next()) {
                    String currentStatus = itemResultSet.getString("Status");
                    double currentUnitsBought = itemResultSet.getDouble("UnitsBought");

                    if (!currentStatus.equalsIgnoreCase(purchaseItemsDto.getStatus())) {
                        if ("Received".equalsIgnoreCase(currentStatus)) {

                            if (!updateInventory(purchaseItemsDto.getInventoryItemID(), -currentUnitsBought)) {
                                connection.rollback();
                                return false;
                            }
                        }
                        if ("Received".equalsIgnoreCase(purchaseItemsDto.getStatus())) {
                            if (!updateInventory(purchaseItemsDto.getInventoryItemID(), purchaseItemsDto.getUnitsBought())) {
                                connection.rollback();
                                return false;
                            }
                        }
                    } else if ("Received".equalsIgnoreCase(currentStatus)) {
                        double unitsDifference = purchaseItemsDto.getUnitsBought() - currentUnitsBought;
                        if (!updateInventory(purchaseItemsDto.getInventoryItemID(), unitsDifference)) {
                            connection.rollback();
                            return false;
                        }
                    }

                    String updatePurchaseItemSQL = "UPDATE PurchaseItems SET Unit = ?, UnitPrice = ?, UnitsBought = ?, Status = ? WHERE PurchaseID = ? AND InventoryItemID = ?";
                    boolean itemUpdated = CrudUtil.execute(updatePurchaseItemSQL,
                            purchaseItemsDto.getUnitPerPrice(),
                            purchaseItemsDto.getUnitPrice(),
                            purchaseItemsDto.getUnitsBought(),
                            purchaseItemsDto.getStatus(),
                            purchaseItemsDto.getPurchaseID(),
                            purchaseItemsDto.getInventoryItemID());

                    if (!itemUpdated) {
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




    /*
      Suggests the next PurchaseID based on the last recorded purchase ID.
     */
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT PurchaseID FROM Purchases ORDER BY PurchaseID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }


    public String deletePurchase(String purchaseID) throws ClassNotFoundException, SQLException {
        Connection connection = null;

        try {
            // Get the database connection
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            String fetchItemsSql = "SELECT InventoryItemID, UnitsBought, Status FROM PurchaseItems WHERE PurchaseID = ?";
            ResultSet resultSet = CrudUtil.execute(fetchItemsSql, purchaseID);

            // Process each item to adjust inventory quantities if the status is "Received"
            while (resultSet.next()) {
                String itemID = resultSet.getString("InventoryItemID");
                double unitsBought = resultSet.getDouble("UnitsBought");
                String status = resultSet.getString("Status");

                // Only adjust inventory if the status is "Received"
                if ("Received".equalsIgnoreCase(status)) {
                    String updateInventorySql = "UPDATE InventoryItems SET Quantity = Quantity - ? WHERE InventoryItemID = ?";
                    boolean updateInventoryResult = CrudUtil.execute(updateInventorySql, unitsBought, itemID);

                    if (!updateInventoryResult) {
                        connection.rollback();
                        return "Failed to update inventory for item: " + itemID;
                    }
                }
            }

            // Step 1: Delete from PurchaseItems table
            String deleteItemsSql = "DELETE FROM PurchaseItems WHERE PurchaseID = ?";
            boolean deleteItemsResult = CrudUtil.execute(deleteItemsSql, purchaseID);

            if (!deleteItemsResult) {
                connection.rollback();
                return "Failed to delete purchase items.";
            }

            // Step 2: Delete from Purchases table
            String deletePurchaseSql = "DELETE FROM Purchases WHERE PurchaseID = ?";
            boolean deletePurchaseResult = CrudUtil.execute(deletePurchaseSql, purchaseID);

            if (!deletePurchaseResult) {
                connection.rollback();
                return "Failed to delete purchase.";
            }

            // Step 3: Commit the transaction if both delete operations are successful
            connection.commit();
            return "Successfully deleted purchase.";

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback transaction on error
            }
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();

        } finally {
            // Reset auto-commit mode to true
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public StockPurchaseTM searchByID(String purchaseID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        // SQL query with JOINs to fetch necessary data
        String query = "SELECT pi.PurchaseID, pi.InventoryItemID, ii.Name AS ItemName, pi.UnitPrice, pi.UnitsBought, " +
                "(pi.UnitPrice * pi.UnitsBought) AS TotalAmount, p.PurchaseDate, p.SupplierID, pi.Status, " +
                "ii.UnitsMeasured, s.Name AS SupplierName, pi.Unit " +
                "FROM PurchaseItems pi " +
                "JOIN InventoryItems ii ON pi.InventoryItemID = ii.InventoryItemID " +
                "JOIN Purchases p ON pi.PurchaseID = p.PurchaseID " +
                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
                "WHERE pi.PurchaseID = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, purchaseID);
            ResultSet rs = pst.executeQuery();

            // Check if a result is found
            if (rs.next()) {
                // Return the populated StockPurchaseTM object
                return new StockPurchaseTM(
                        rs.getString("PurchaseID"),
                        rs.getString("InventoryItemID"),
                        rs.getString("ItemName"),
                        rs.getDouble("UnitPrice"),
                        rs.getDouble("UnitsBought"),
                        rs.getDouble("TotalAmount"),
                        rs.getDate("PurchaseDate").toLocalDate(),
                        rs.getString("SupplierID"),
                        rs.getString("Status"),
                        rs.getString("SupplierName"),
                        rs.getDouble("Unit"),
                        rs.getString("UnitsMeasured")
                );
            }
        }

        // Return null if no matching record is found
        return null;
    }





}


