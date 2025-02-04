package org.gourmetDelight.dao.custom.impl.reservations;

import org.gourmetDelight.dao.custom.TableAssignmentsDAO;
import org.gourmetDelight.entity.TableAssignments;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableAssignmentsDAOImpl implements TableAssignmentsDAO {


    @Override
    public ArrayList getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(TableAssignments dto) throws ClassNotFoundException, SQLException {
        String assignmentSql = "INSERT INTO TableAssignments (TableID, ReservationID, AssignmentTime) VALUES (?, ?, ?)";
        return SQLUtil.execute(assignmentSql, dto.getTableID(), dto.getReservationId(), dto.getAssignDateTime());

    }

    @Override
    public boolean delete(String id) throws ClassNotFoundException, SQLException {
        String deleteAssignmentSql = "DELETE FROM TableAssignments WHERE ReservationID = ?";
        return SQLUtil.execute(deleteAssignmentSql, id);
    }

    @Override
    public boolean update(TableAssignments dto) throws ClassNotFoundException, SQLException {
        String updateAssignmentSql = "UPDATE TableAssignments SET TableID = ?, AssignmentTime = ? WHERE ReservationID = ?";
        return SQLUtil.execute(updateAssignmentSql, dto.getTableID(), dto.getAssignDateTime(), dto.getReservationId());

    }

    @Override
    public TableAssignments searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ResultSet searchByReserveId(String id) throws ClassNotFoundException, SQLException {
        String getTableIdSql = "SELECT TableID FROM TableAssignments WHERE ReservationID = ?";
        ResultSet resultSet = SQLUtil.execute(getTableIdSql, id);
        return resultSet;
    }


    @Override
    public ArrayList searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }
}
