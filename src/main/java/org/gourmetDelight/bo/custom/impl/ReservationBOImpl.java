package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.ReservationBO;
import org.gourmetDelight.dao.custom.QueryDAO;
import org.gourmetDelight.dao.custom.ReservationDAO;
import org.gourmetDelight.dao.custom.impl.QueryDAOImpl;
import org.gourmetDelight.dao.custom.impl.reservations.ReservationDAOImpl;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.entity.Reservation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReservationBOImpl implements ReservationBO {
    ReservationDAO reservationDAO = new ReservationDAOImpl();
    QueryDAO queryDAO = new QueryDAOImpl();

    @Override
    public ArrayList getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Object dto) throws ClassNotFoundException, SQLException {
        return false;
    }


    public boolean save(ReservationDto dto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException {
        Reservation reservation = new Reservation(dto.getReservationID(),dto.getCustomerID(),dto.getReservationDate(),dto.getNumberOfGuests(),dto.getSpecialRequests(),dto.getStatus());
        return reservationDAO.save(reservation, tableID, assignDateTime);
    }

    public boolean delete(String id) throws ClassNotFoundException, SQLException {
        return reservationDAO.delete(id);
    }

    @Override
    public boolean update(Object dto) throws ClassNotFoundException, SQLException {
        return false;
    }



    public boolean update(ReservationDto dto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException {
        Reservation reservation = new Reservation(dto.getReservationID(),dto.getCustomerID(),dto.getReservationDate(),dto.getNumberOfGuests(),dto.getSpecialRequests(),dto.getStatus());
        return reservationDAO.update(reservation, tableID, assignDateTime);
    }


    @Override
    public ArrayList searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }



    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return reservationDAO.suggestNextID();
    }



    public boolean updateReservationStatus(String id, String newStatus) throws ClassNotFoundException, SQLException {
        return reservationDAO.updateReservationStatus(id, newStatus);
    }


    public ReservationDto searchById(String id) throws ClassNotFoundException, SQLException {
        ReservationDto reservation = queryDAO.searchById(id);
        ReservationDto dto = new ReservationDto();
        dto.setReservationID(reservation.getReservationID());
        dto.setCustomerID(reservation.getCustomerID());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setSpecialRequests(reservation.getSpecialRequests());
        dto.setStatus(reservation.getStatus());

        return dto;
    }

    public LocalDateTime findDateTime(String reservationID) throws SQLException, ClassNotFoundException {
        return queryDAO.findDateTime(reservationID);
    }

    public ArrayList<ReservationDto> searchReservationsByCustomerID(String customerID) throws ClassNotFoundException, SQLException {
        return queryDAO.searchReservationsByCustomerID(customerID);
    }

    public ReservationDto searchReservationsByReserveID(String id) throws ClassNotFoundException, SQLException {
        return queryDAO.searchById(id);
    }




    public ArrayList<ReservationDto> getAllReservationDetails() throws ClassNotFoundException, SQLException {
        return queryDAO.getAllReservationDetails();
    }


}
