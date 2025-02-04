package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.Reservation;

import java.sql.SQLException;
import java.time.LocalDateTime;

public interface ReservationDAO extends CrudDAO {
    public boolean save(Reservation reservationDto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException;

    public boolean updateReservationStatus(String reservationID, String newStatus) throws ClassNotFoundException, SQLException;

    public boolean update(Reservation dto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException;

}
