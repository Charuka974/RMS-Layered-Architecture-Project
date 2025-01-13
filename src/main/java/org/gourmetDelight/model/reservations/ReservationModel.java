package org.gourmetDelight.model.reservations;

import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.dto.reservations.TablesDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReservationModel {

    // 1. Get all reservations with associated Table ID
    public ArrayList<ReservationDto> getAllReservations() throws ClassNotFoundException, SQLException {
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

    // 2. Save a new reservation
    public String saveReservation(ReservationDto reservationDto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException {
        // Step 1: Insert the reservation into the Reservations table
        String sql = "INSERT INTO Reservations (ReservationID, CustomerID, ReservationDate, NumberOfGuests, SpecialRequests, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        boolean result = CrudUtil.execute(sql,
                reservationDto.getReservationID(),
                reservationDto.getCustomerID(),
                reservationDto.getReservationDate(),
                reservationDto.getNumberOfGuests(),
                reservationDto.getSpecialRequests(),
                reservationDto.getStatus()
        );

        if (result) {
            // Step 2: Insert the table assignment
            String assignmentSql = "INSERT INTO TableAssignments (TableID, ReservationID, AssignmentTime) VALUES (?, ?, ?)";
            boolean assignmentResult = CrudUtil.execute(assignmentSql, tableID, reservationDto.getReservationID(), assignDateTime);

            if (assignmentResult) {
                // Step 3: Update the table status to "Reserved"
                String updateTableStatusSql = "UPDATE Tables SET Status = 'Reserved' WHERE TableID = ?";
                boolean statusUpdated = CrudUtil.execute(updateTableStatusSql, tableID);

                return statusUpdated ? "Reservation and Table Assignment saved successfully, table status updated to Reserved" : "Failed to update table status";
            }

            return "Failed to assign table";
        }

        return "Failed to save reservation";
    }


    // 3. Delete a reservation
    public String deleteReservation(String reservationID) throws ClassNotFoundException, SQLException {
        // Step 1: Retrieve the TableID(s) associated with the reservation before deleting the assignment
        String getTableIdSql = "SELECT TableID FROM TableAssignments WHERE ReservationID = ?";
        ResultSet resultSet = CrudUtil.execute(getTableIdSql, reservationID);

        // Step 2: Store the TableID(s) in a list
        ArrayList<String> tableIDs = new ArrayList<>();
        while (resultSet.next()) {
            tableIDs.add(resultSet.getString("TableID"));
        }

        // Step 3: Delete the table assignment related to the reservation
        String deleteAssignmentSql = "DELETE FROM TableAssignments WHERE ReservationID = ?";
        boolean assignmentDeleted = CrudUtil.execute(deleteAssignmentSql, reservationID);

        if (assignmentDeleted) {
            // Step 4: Update the status of the tables to "Available"
            for (String tableID : tableIDs) {
                String updateTableStatusSql = "UPDATE Tables SET Status = 'Available' WHERE TableID = ?";
                CrudUtil.execute(updateTableStatusSql, tableID);
            }

            // Step 5: Delete the reservation
            String deleteReservationSql = "DELETE FROM Reservations WHERE ReservationID = ?";
            boolean reservationDeleted = CrudUtil.execute(deleteReservationSql, reservationID);

            return reservationDeleted ? "Reservation deleted successfully and table status updated"
                    : "Failed to delete reservation";
        }

        return "Failed to delete table assignment";
    }



    // 4. Update a reservation
    public String updateReservation(ReservationDto reservationDto, String newTableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Reservations SET CustomerID = ?, ReservationDate = ?, NumberOfGuests = ?, SpecialRequests = ?, Status = ? " +
                "WHERE ReservationID = ?";
        boolean result = CrudUtil.execute(sql,
                reservationDto.getCustomerID(),
                reservationDto.getReservationDate(),
                reservationDto.getNumberOfGuests(),
                reservationDto.getSpecialRequests(),
                reservationDto.getStatus(),
                reservationDto.getReservationID()
        );

        if (result) {
            String updateAssignmentSql = "UPDATE TableAssignments SET TableID = ?, AssignmentTime = ? WHERE ReservationID = ?";
            boolean assignmentUpdated = CrudUtil.execute(updateAssignmentSql, newTableID, assignDateTime, reservationDto.getReservationID());
            return assignmentUpdated ? "Reservation and Table Assignment updated successfully" : "Failed to update table assignment";
        }

        return "Failed to update reservation";
    }

    // 5. Search reservation by ID
    public ReservationDto searchReservationById(String reservationID) throws ClassNotFoundException, SQLException {
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


    // 6. Suggest the next reservation ID
    public String suggestNextReservationID() throws ClassNotFoundException, SQLException {
        String sql = "SELECT ReservationID FROM Reservations ORDER BY ReservationID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);

        if (resultSet.next()) {
            String lastId = resultSet.getString("ReservationID");
            String idNumber = lastId.substring(1);
            int newIdIndex = Integer.parseInt(idNumber) + 1;
            resultSet.close();
            return String.format("R%03d", newIdIndex);
        }

        resultSet.close();
        return "R001";
    }

    // 7. Search reservations by customer ID
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


    public boolean updateReservationStatus(String reservationID, String newStatus) throws ClassNotFoundException, SQLException {
        if (reservationID == null || reservationID.isEmpty()) {
            throw new IllegalArgumentException("ReservationID cannot be null or empty.");
        }
        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        String sql = "UPDATE Reservations SET Status = ? WHERE ReservationID = ?";

        try {
            return CrudUtil.execute(sql, newStatus, reservationID);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating reservation status: " + e.getMessage());
            throw e;
        }
    }



}
