package org.gourmetDelight.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.gourmetDelight.dao.SuperDAO;
import org.gourmetDelight.dao.custom.QueryDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.dto.tm.StockPurchaseTM;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.util.CrudUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {


    public String selectEmail(String username) throws SQLException, ClassNotFoundException  {

        String email = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Email " +
                "FROM Users u " +
                "JOIN Employees e ON u.EmployeeID = e.EmployeeID " +
                "WHERE u.Username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    email = result.getString("Email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }

    public String selectPhone(String username) throws SQLException, ClassNotFoundException {
        String phoneNumber = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Phone " +
                "FROM Users u " +
                "JOIN Employees e ON u.EmployeeID = e.EmployeeID " +
                "WHERE u.Username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    phoneNumber = result.getString("Phone");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phoneNumber;

    }

    public String getRole(String username, String password) throws ClassNotFoundException, SQLException {
        String role = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Position " +
                "FROM Users u " +
                "JOIN Employees e ON u.EmployeeID = e.EmployeeID " +
                "WHERE u.Username = ? AND u.Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    role = result.getString("Position");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    // Retrieve all the data
    //-----------------------------------------------------------------------------------------------------

    public ObservableList<StockPurchaseTM> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT p.PurchaseID, " +
                "pi.InventoryItemID, " +
                "ii.Name AS ItemName, " +
                "pi.UnitPrice, " +
                "pi.UnitsBought, " +
                "(pi.UnitPrice * pi.UnitsBought) AS TotalAmount, " +
                "p.PurchaseDate, " +
                "p.SupplierID, " +
                "pi.Status, " +
                "ii.UnitsMeasured, " +
                "s.Name AS SupplierName, " +
                "pi.Unit " +
                "FROM Purchases p " +
                "JOIN PurchaseItems pi ON p.PurchaseID = pi.PurchaseID " +
                "JOIN InventoryItems ii ON pi.InventoryItemID = ii.InventoryItemID " +
                "JOIN Suppliers s ON p.SupplierID = s.SupplierID";

        ResultSet resultSet = CrudUtil.execute(sql);
        ArrayList<StockPurchaseTM> purchaseList = new ArrayList<>();

        while (resultSet.next()) {
            String purchaseID = resultSet.getString("PurchaseID");
            String itemID = resultSet.getString("InventoryItemID");
            String itemName = resultSet.getString("ItemName");
            double unitPrice = resultSet.getDouble("UnitPrice");
            double quantity = resultSet.getDouble("UnitsBought");
            double totalAmount = resultSet.getDouble("TotalAmount");
            LocalDate purchaseDate = resultSet.getDate("PurchaseDate").toLocalDate();
            String supplierID = resultSet.getString("SupplierID");
            String status = resultSet.getString("Status");

            String supplierName = resultSet.getString("SupplierName");
            double unit = resultSet.getDouble("Unit");
            String unitsMeasured = resultSet.getString("UnitsMeasured");


            StockPurchaseTM purchase = new StockPurchaseTM(
                    purchaseID, itemID, itemName, unitPrice, quantity, totalAmount, purchaseDate, supplierID, status, supplierName, unit, unitsMeasured
            );

            purchaseList.add(purchase);
        }

        resultSet.close();

        return FXCollections.observableArrayList(purchaseList);
    }




    // Reservation -----------------------------------------------------------------------------------------------------
    public ReservationDto searchById(String reservationID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT r.ReservationID, r.CustomerID, r.ReservationDate, r.NumberOfGuests, " +
                "r.SpecialRequests, r.Status, ta.TableID " +
                "FROM Reservations r " +
                "LEFT JOIN TableAssignments ta ON r.ReservationID = ta.ReservationID " +
                "WHERE r.ReservationID = ?";

        ResultSet resultSet = CrudUtil.execute(sql, reservationID);

        if (resultSet.next()) {
            ReservationDto reservation = new ReservationDto(
                    resultSet.getString("ReservationID"),
                    resultSet.getString("CustomerID"),
                    resultSet.getDate("ReservationDate").toLocalDate(),
                    resultSet.getInt("NumberOfGuests"),
                    resultSet.getString("SpecialRequests"),
                    resultSet.getString("Status"),
                    resultSet.getString("TableID")
            );
            resultSet.close();
            return reservation;
        }

        resultSet.close();
        return null;
    }

    public LocalDateTime findDateTime(String reservationID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT ta.AssignmentTime " +
                "FROM Reservations r " +
                "LEFT JOIN TableAssignments ta ON r.ReservationID = ta.ReservationID " +
                "WHERE r.ReservationID = ?";

        try (ResultSet resultSet = CrudUtil.execute(sql, reservationID)) {
            if (resultSet.next()) {
                // Assuming the column "AssignmentTime" is of type TIMESTAMP in the database
                Timestamp timestamp = resultSet.getTimestamp("AssignmentTime");

                if (timestamp != null) {
                    return timestamp.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            throw e;
        }

        return null;
    }

    public ArrayList<ReservationDto> searchReservationsByCustomerID(String customerID) throws ClassNotFoundException, SQLException {
        // SQL query to fetch all reservations for the given customer ID
        String sql = "SELECT r.ReservationID, r.CustomerID, r.ReservationDate, r.NumberOfGuests, " +
                "r.SpecialRequests, r.Status, ta.TableID " +
                "FROM Reservations r " +
                "LEFT JOIN TableAssignments ta ON r.ReservationID = ta.ReservationID " +
                "WHERE r.CustomerID = ?";

        // Execute the query with the provided customer ID
        ResultSet resultSet = CrudUtil.execute(sql, customerID);

        // List to store reservations for the customer
        ArrayList<ReservationDto> reservationList = new ArrayList<>();

        // Iterate through the result set and populate the reservationList
        while (resultSet.next()) {
            ReservationDto reservation = new ReservationDto(
                    resultSet.getString("ReservationID"),
                    resultSet.getString("CustomerID"),
                    resultSet.getDate("ReservationDate").toLocalDate(),
                    resultSet.getInt("NumberOfGuests"),
                    resultSet.getString("SpecialRequests"),
                    resultSet.getString("Status"),
                    resultSet.getString("TableID")
            );
            reservationList.add(reservation);  // Add the reservation to the list
        }

        // Close the result set after processing
        resultSet.close();

        // Return the list of reservations
        return reservationList;
    }

    public ReservationDto searchReservationsByReserveID(String resId) throws ClassNotFoundException, SQLException {
        // SQL query to fetch all reservations for the given reservation ID
        String sql = "SELECT r.ReservationID, r.CustomerID, r.ReservationDate, r.NumberOfGuests, " +
                "r.SpecialRequests, r.Status, ta.TableID " +
                "FROM Reservations r " +
                "LEFT JOIN TableAssignments ta ON r.ReservationID = ta.ReservationID " +
                "WHERE r.ReservationID = ?";

        // Execute the query with the provided reservation ID
        ResultSet resultSet = CrudUtil.execute(sql, resId);

        // Move the cursor to the first row
        if (resultSet.next()) {  // Ensure there's a record before accessing data
            return new ReservationDto(
                    resultSet.getString("ReservationID"),
                    resultSet.getString("CustomerID"),
                    resultSet.getDate("ReservationDate").toLocalDate(),
                    resultSet.getInt("NumberOfGuests"),
                    resultSet.getString("SpecialRequests"),
                    resultSet.getString("Status"),
                    resultSet.getString("TableID")
            );
        }

        // If no record is found, return null
        return null;
    }




    public ArrayList<ReservationDto> getAllReservationDetails() throws ClassNotFoundException, SQLException {
        String sql = "SELECT r.ReservationID, r.CustomerID, r.ReservationDate, r.NumberOfGuests, " +
                "r.SpecialRequests, r.Status, ta.TableID " +
                "FROM Reservations r " +
                "LEFT JOIN TableAssignments ta ON r.ReservationID = ta.ReservationID";

        ResultSet resultSet = CrudUtil.execute(sql);
        ArrayList<ReservationDto> reservations = new ArrayList<>();

        while (resultSet.next()) {
            ReservationDto reservation = new ReservationDto(
                    resultSet.getString("ReservationID"),
                    resultSet.getString("CustomerID"),
                    resultSet.getDate("ReservationDate").toLocalDate(),
                    resultSet.getInt("NumberOfGuests"),
                    resultSet.getString("SpecialRequests"),
                    resultSet.getString("Status"),
                    resultSet.getString("TableID")
            );
            reservations.add(reservation);
        }

        resultSet.close();
        return reservations;
    }



}
