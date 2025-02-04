package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.TableBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.CustomerDAO;
import org.gourmetDelight.dao.custom.TableDAO;
import org.gourmetDelight.dao.custom.impl.reservations.TableDAOImpl;
import org.gourmetDelight.dto.employee.EmployeeDto;
import org.gourmetDelight.dto.reservations.TablesDto;
import org.gourmetDelight.entity.Employee;
import org.gourmetDelight.entity.Tables;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableBOImpl implements TableBO {
    TableDAO tableDAO = (TableDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.TABLE);

    public ArrayList<TablesDto> getAll() throws ClassNotFoundException, SQLException {
        ArrayList<Tables> tables = tableDAO.getAll();
        ArrayList<TablesDto> tableDTOs = new ArrayList<>();
        for (Tables table : tables) {
            TablesDto tableDTO = new TablesDto();
            tableDTO.setTableID(table.getTableID());
            tableDTO.setTableNumber(table.getTableNumber());
            tableDTO.setCapacity(table.getCapacity());
            tableDTO.setLocation(table.getLocation());
            tableDTO.setStatus(table.getStatus());
            tableDTOs.add(tableDTO);
        }
        return tableDTOs;
    }

    @Override
    public boolean save(TablesDto dto) throws ClassNotFoundException, SQLException {
        Tables table = new Tables(dto.getTableID(),dto.getTableNumber(),dto.getCapacity(),dto.getLocation(),dto.getStatus());
        return tableDAO.save(table);
    }


    @Override
    public boolean delete(String id) throws ClassNotFoundException, SQLException {
        return tableDAO.delete(id);
    }


    @Override
    public boolean update(TablesDto dto) throws ClassNotFoundException, SQLException {
        Tables table = new Tables(dto.getTableID(),dto.getTableNumber(),dto.getCapacity(),dto.getLocation(),dto.getStatus());
        return tableDAO.update(table);
    }

    @Override
    public TablesDto searchById(String tableID) throws ClassNotFoundException, SQLException {
        Tables table = tableDAO.searchById(tableID);
        TablesDto tableDTO = new TablesDto();
        tableDTO.setTableID(table.getTableID());
        tableDTO.setTableNumber(table.getTableNumber());
        tableDTO.setCapacity(table.getCapacity());
        tableDTO.setLocation(table.getLocation());
        tableDTO.setStatus(table.getStatus());
        return tableDTO;
    }

    @Override
    public ArrayList<TablesDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }


    public ArrayList<TablesDto> searchTablesByLocation(String location) throws ClassNotFoundException, SQLException {
        ArrayList<Tables> tables = tableDAO.searchTablesByLocation(location);
        ArrayList<TablesDto> tableDTOs = new ArrayList<>();
        for (Tables table : tables) {
            TablesDto tableDTO = new TablesDto();
            tableDTO.setTableID(table.getTableID());
            tableDTO.setTableNumber(table.getTableNumber());
            tableDTO.setCapacity(table.getCapacity());
            tableDTO.setLocation(table.getLocation());
            tableDTO.setStatus(table.getStatus());
            tableDTOs.add(tableDTO);
        }
        return tableDTOs;
    }

    public ArrayList<TablesDto> searchTablesByAvailability(String status) throws ClassNotFoundException, SQLException {
        ArrayList<Tables> tables = tableDAO.searchTablesByAvailability(status);
        ArrayList<TablesDto> tableDTOs = new ArrayList<>();
        for (Tables table : tables) {
            TablesDto tableDTO = new TablesDto();
            tableDTO.setTableID(table.getTableID());
            tableDTO.setTableNumber(table.getTableNumber());
            tableDTO.setCapacity(table.getCapacity());
            tableDTO.setLocation(table.getLocation());
            tableDTO.setStatus(table.getStatus());
            tableDTOs.add(tableDTO);
        }
        return tableDTOs;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return tableDAO.suggestNextID();
    }



    public ArrayList<TablesDto> findAvailableTable(int capacity) throws ClassNotFoundException, SQLException {
        ArrayList<Tables> tables = tableDAO.findAvailableTable(capacity);
        ArrayList<TablesDto> tableDTOs = new ArrayList<>();
        for (Tables table : tables) {
            TablesDto tableDTO = new TablesDto();
            tableDTO.setTableID(table.getTableID());
            tableDTO.setTableNumber(table.getTableNumber());
            tableDTO.setCapacity(table.getCapacity());
            tableDTO.setLocation(table.getLocation());
            tableDTO.setStatus(table.getStatus());
            tableDTOs.add(tableDTO);
        }
        return tableDTOs;
    }


    public boolean updateTableStatus(String tableID, String newStatus) throws SQLException, ClassNotFoundException {
        return tableDAO.updateTableStatus(tableID, newStatus);
    }


}
