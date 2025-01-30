package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.entity.Reservation;
import org.gourmetDelight.entity.TableAssignments;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ReservationDAO extends CrudDAO {
    public boolean save(Reservation reservationDto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException;

    public boolean updateReservationStatus(String reservationID, String newStatus) throws ClassNotFoundException, SQLException;

    public boolean update(Reservation dto, String tableID, LocalDateTime assignDateTime) throws ClassNotFoundException, SQLException;

}
