package org.gourmetDelight.bo.custom;

import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.reservations.TablesDto;
import org.gourmetDelight.entity.Tables;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TableBO extends SuperBO {

    public ArrayList<TablesDto> getAll() throws ClassNotFoundException, SQLException;


    public boolean save(TablesDto tablesDto) throws ClassNotFoundException, SQLException;


    public boolean delete(String tableID) throws ClassNotFoundException, SQLException;


    public boolean update(TablesDto tablesDto) throws ClassNotFoundException, SQLException;


    public TablesDto searchById(String tableID) throws ClassNotFoundException, SQLException;


    public ArrayList<TablesDto> searchByName(String name) throws ClassNotFoundException, SQLException;


    public ArrayList<TablesDto> searchTablesByLocation(String location) throws ClassNotFoundException, SQLException;

    public ArrayList<TablesDto> searchTablesByAvailability(String status) throws ClassNotFoundException, SQLException;


    public String suggestNextID() throws ClassNotFoundException, SQLException;


    public ArrayList<TablesDto> findAvailableTable(int capacity) throws ClassNotFoundException, SQLException;

    public boolean updateTableStatus(String tableID, String newStatus) throws SQLException, ClassNotFoundException;

}
