package org.gourmetDelight.dao.custom.impl.reservations;

import org.gourmetDelight.dao.custom.QueryDAO;
import org.gourmetDelight.dao.custom.ReservationDAO;
import org.gourmetDelight.dao.custom.TableAssignmentsDAO;
import org.gourmetDelight.dao.custom.TableDAO;
import org.gourmetDelight.dao.custom.impl.QueryDAOImpl;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.entity.TableAssignments;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReservationDAOImpl implements ReservationDAO {
    TableAssignmentsDAO tableAssignmentsDAOImpl = new TableAssignmentsDAOImpl();
    TableDAO tableDAOImpl = new TableDAOImpl();


    @Override
    public ArrayList getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Object dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    // 2. Save a new reservation
    public boolean save(Reservation reservationDto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException {
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
            if (tableAssignmentsDAOImpl.save(new TableAssignments(tableID, reservationDto.getReservationID(), assignDateTime))) {
                // Step 3: Update the table status to "Reserved"
                return tableDAOImpl.updateTableStatus(tableID, "Reserved");
            }

            return false;
        }

        return false;
    }


    // 3. Delete a reservation
    public boolean delete(String reservationID) throws ClassNotFoundException, SQLException {
        // Step 1: Retrieve the TableID(s) associated with the reservation before deleting the assignment
        ResultSet resultSet = tableAssignmentsDAOImpl.searchByReserveId(reservationID);


        // Step 2: Store the TableID(s) in a list
        ArrayList<String> tableIDs = new ArrayList<>();
        while (resultSet.next()) {
            tableIDs.add(resultSet.getString("TableID"));
        }

        // Step 3: Delete the table assignment related to the reservation
        if (tableAssignmentsDAOImpl.delete(reservationID)) {
            // Step 4: Update the status of the tables to "Available"
            for (String tableID : tableIDs) {
                tableDAOImpl.updateTableStatus(tableID, "Available");
            }

            // Step 5: Delete the reservation
            String deleteReservationSql = "DELETE FROM Reservations WHERE ReservationID = ?";
            boolean reservationDeleted = CrudUtil.execute(deleteReservationSql, reservationID);

            return reservationDeleted;
        }

        return false;
    }

    @Override
    public boolean update(Object dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public Reservation searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }


    // 4. Update a reservation
    public boolean update(Reservation reservationDto, String newTableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException {
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
            return tableAssignmentsDAOImpl.update(new TableAssignments(newTableID, reservationDto.getReservationID(),assignDateTime));

        }

        return false;
    }


    @Override
    public ArrayList searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }



    // 6. Suggest the next reservation ID
    public String suggestNextID() throws ClassNotFoundException, SQLException {
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
