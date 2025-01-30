package org.gourmetDelight.bo.custom;

import org.gourmetDelight.dao.custom.TableAssignmentsDAO;
import org.gourmetDelight.dao.custom.TableDAO;
import org.gourmetDelight.dao.custom.impl.reservations.TableAssignmentsDAOImpl;
import org.gourmetDelight.dao.custom.impl.reservations.TableDAOImpl;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.entity.TableAssignments;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ReservationBO {
    TableAssignmentsDAO tableAssignmentsDAOImpl = new TableAssignmentsDAOImpl();
    TableDAO tableDAOImpl = new TableDAOImpl();


    public ArrayList getAll() throws ClassNotFoundException, SQLException;

    public boolean save(Object dto) throws ClassNotFoundException, SQLException;

    public boolean save(ReservationDto reservationDto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException;

    public boolean delete(String reservationID) throws ClassNotFoundException, SQLException;

    public boolean update(Object dto) throws ClassNotFoundException, SQLException;

    public ReservationDto searchById(String Id) throws ClassNotFoundException, SQLException;


    public boolean update(ReservationDto reservationDto, String newTableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException;


    public ArrayList searchByName(String name) throws ClassNotFoundException, SQLException;


    public String suggestNextID() throws ClassNotFoundException, SQLException;


    public boolean updateReservationStatus(String reservationID, String newStatus) throws ClassNotFoundException, SQLException;


    public LocalDateTime findDateTime(String reservationID) throws SQLException, ClassNotFoundException;

    public ArrayList<ReservationDto> searchReservationsByCustomerID(String customerID) throws ClassNotFoundException, SQLException;

    public ReservationDto searchReservationsByReserveID(String resId) throws ClassNotFoundException, SQLException;

    public ArrayList<ReservationDto> getAllReservationDetails() throws ClassNotFoundException, SQLException;


}
