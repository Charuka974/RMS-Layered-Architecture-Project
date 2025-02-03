package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.SuperDAO;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.entity.custom.ReservationCustom;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    public ReservationCustom searchReservationsByReserveID(String customerID) throws ClassNotFoundException, SQLException;

    public ReservationCustom searchById(String reservationID) throws ClassNotFoundException, SQLException;
    public LocalDateTime findDateTime(String reservationID) throws SQLException, ClassNotFoundException;

    public ArrayList<ReservationCustom> searchReservationsByCustomerID(String customerID) throws ClassNotFoundException, SQLException;
    public ArrayList<ReservationCustom> getAllReservationDetails() throws ClassNotFoundException, SQLException;
}
