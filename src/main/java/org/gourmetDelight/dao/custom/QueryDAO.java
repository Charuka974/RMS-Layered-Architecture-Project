package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.SuperDAO;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    public ReservationDto searchReservationsByReserveID(String customerID) throws ClassNotFoundException, SQLException;

    public ReservationDto searchById(String reservationID) throws ClassNotFoundException, SQLException;
    public LocalDateTime findDateTime(String reservationID) throws SQLException, ClassNotFoundException;

    public ArrayList<ReservationDto> searchReservationsByCustomerID(String customerID) throws ClassNotFoundException, SQLException;
    public ArrayList<ReservationDto> getAllReservationDetails() throws ClassNotFoundException, SQLException;
}
