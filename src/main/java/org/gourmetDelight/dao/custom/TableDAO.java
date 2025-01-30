package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.Tables;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TableDAO extends CrudDAO<Tables> {
    public ArrayList<Tables> searchTablesByLocation(String location) throws ClassNotFoundException, SQLException;

    public ArrayList<Tables> searchTablesByAvailability(String status) throws ClassNotFoundException, SQLException;

    public ArrayList<Tables> findAvailableTable(int capacity) throws ClassNotFoundException, SQLException;

    public boolean updateTableStatus(String tableID, String newStatus) throws SQLException, ClassNotFoundException;
}