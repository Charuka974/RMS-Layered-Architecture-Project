package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.ReservationBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.CustomerDAO;
import org.gourmetDelight.dao.custom.QueryDAO;
import org.gourmetDelight.dao.custom.ReservationDAO;
import org.gourmetDelight.dao.custom.impl.QueryDAOImpl;
import org.gourmetDelight.dao.custom.impl.reservations.ReservationDAOImpl;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.entity.Customer;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.entity.custom.ReservationCustom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReservationBOImpl implements ReservationBO {
    ReservationDAO reservationDAO = (ReservationDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.RESERVATIONS);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);

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

        ReservationCustom reservation = queryDAO.searchById(id);
        if (reservation == null) {
            return null;
        }
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
        ArrayList<ReservationCustom> reservations = queryDAO.searchReservationsByCustomerID(customerID);
        ArrayList<ReservationDto> reservationDTOs = new ArrayList<>();
        for (ReservationCustom reservation : reservations) {
            ReservationDto dto = new ReservationDto();
            dto.setReservationID(reservation.getReservationID());
            dto.setCustomerID(reservation.getCustomerID());
            dto.setReservationDate(reservation.getReservationDate());
            dto.setNumberOfGuests(reservation.getNumberOfGuests());
            dto.setSpecialRequests(reservation.getSpecialRequests());
            dto.setStatus(reservation.getStatus());
            reservationDTOs.add(dto);
        }
        return reservationDTOs;
    }

    public ReservationDto searchReservationsByReserveID(String id) throws ClassNotFoundException, SQLException {
        ReservationCustom reservation = queryDAO.searchById(id);
        ReservationDto dto = new ReservationDto();
        dto.setReservationID(reservation.getReservationID());
        dto.setCustomerID(reservation.getCustomerID());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setSpecialRequests(reservation.getSpecialRequests());
        dto.setStatus(reservation.getStatus());

        return dto;
    }




    public ArrayList<ReservationDto> getAllReservationDetails() throws ClassNotFoundException, SQLException {
        ArrayList<ReservationCustom> reservations = queryDAO.getAllReservationDetails();
        ArrayList<ReservationDto> reservationDTOs = new ArrayList<>();
        for (ReservationCustom reservation : reservations) {
            ReservationDto dto = new ReservationDto();
            dto.setReservationID(reservation.getReservationID());
            dto.setCustomerID(reservation.getCustomerID());
            dto.setReservationDate(reservation.getReservationDate());
            dto.setNumberOfGuests(reservation.getNumberOfGuests());
            dto.setSpecialRequests(reservation.getSpecialRequests());
            dto.setStatus(reservation.getStatus());
            reservationDTOs.add(dto);
        }
        return reservationDTOs;
    }

    public ReservationCustom getJoinReservationDetails(String id) throws ClassNotFoundException, SQLException {
        return queryDAO.searchReservationsByReserveID(id);
    }


}
