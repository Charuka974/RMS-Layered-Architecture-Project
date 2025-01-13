package org.gourmetDelight.model.reservations;

import org.gourmetDelight.dto.reservations.TablesDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableModel {

    public ArrayList<TablesDto> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT TableID, TableNumber, Capacity, Location, Status FROM Tables";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<TablesDto> tables = new ArrayList<>();

        while (resultSet.next()) {
            String tableID = resultSet.getString("TableID");
            String tableNumber = resultSet.getString("TableNumber");
            int capacity = resultSet.getInt("Capacity");
            String location = resultSet.getString("Location");
            String status = resultSet.getString("Status");

            TablesDto table = new TablesDto(tableID, tableNumber, capacity, location, status);
            tables.add(table);
        }

        resultSet.close();
        return tables;
    }


    public String saveTable(TablesDto tablesDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Tables (TableID, TableNumber, Capacity, Location, Status) VALUES (?, ?, ?, ?, ?)";
        boolean result = CrudUtil.execute(sql,
                tablesDto.getTableID(),
                tablesDto.getTableNumber(),
                tablesDto.getCapacity(),
                tablesDto.getLocation(),
                tablesDto.getStatus()
        );
        return result ? "Successfully saved" : "Failed to save";
    }


    public String deleteTable(String tableID) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Tables WHERE TableID = ?";
        boolean result = CrudUtil.execute(sql, tableID);
        return result ? "Successfully deleted" : "Failed to delete";
    }


    public String updateTable(TablesDto tablesDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Tables SET TableNumber = ?, Capacity = ?, Location = ?, Status = ? WHERE TableID = ?";
        boolean result = CrudUtil.execute(sql,
                tablesDto.getTableNumber(),
                tablesDto.getCapacity(),
                tablesDto.getLocation(),
                tablesDto.getStatus(),
                tablesDto.getTableID()
        );
        return result ? "Successfully updated" : "Failed to update";
    }


    public TablesDto searchTableById(String tableID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Tables WHERE TableID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, tableID);

        if (resultSet.next()) {
            TablesDto table = new TablesDto(
                    resultSet.getString("TableID"),
                    resultSet.getString("TableNumber"),
                    resultSet.getInt("Capacity"),
                    resultSet.getString("Location"),
                    resultSet.getString("Status")
            );
            resultSet.close();
            return table;
        } else {
            resultSet.close();
            return null;
        }
    }


    public ArrayList<TablesDto> searchTablesByLocation(String location) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Tables WHERE Location LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + location.toLowerCase() + "%");

        ArrayList<TablesDto> tables = new ArrayList<>();

        while (resultSet.next()) {
            TablesDto dto = new TablesDto(
                    resultSet.getString("TableID"),
                    resultSet.getString("TableNumber"),
                    resultSet.getInt("Capacity"),
                    resultSet.getString("Location"),
                    resultSet.getString("Status")
            );
            tables.add(dto);
        }

        resultSet.close();
        return tables;
    }

    public ArrayList<TablesDto> searchTablesByAvailability(String status) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Tables WHERE Status = ?";
        ResultSet resultSet = CrudUtil.execute(sql, status);

        ArrayList<TablesDto> tables = new ArrayList<>();

        while (resultSet.next()) {
            TablesDto dto = new TablesDto(
                    resultSet.getString("TableID"),
                    resultSet.getString("TableNumber"),
                    resultSet.getInt("Capacity"),
                    resultSet.getString("Location"),
                    resultSet.getString("Status")
            );
            tables.add(dto);
        }

        resultSet.close();
        return tables;
    }


    public String suggestNextTableID() throws ClassNotFoundException, SQLException {
        String sql = "SELECT TableID FROM Tables ORDER BY TableID DESC LIMIT 1";
        ResultSet resultSet = CrudUtil.execute(sql);

        if (resultSet.next()) {
            String lastId = resultSet.getString("TableID");
            String idNumber = lastId.substring(1);
            int newIdIndex = Integer.parseInt(idNumber) + 1;
            return String.format("T%03d", newIdIndex);
        }
        return "T001";
    }



    public ArrayList<TablesDto> findAvailableTable(int capacity) throws ClassNotFoundException, SQLException {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }

        String sql = "SELECT * FROM Tables WHERE Status = ? AND Capacity >= ?";
        ArrayList<TablesDto> tables = new ArrayList<>();

        try (ResultSet resultSet = CrudUtil.execute(sql, "Available", capacity)) {
            while (resultSet.next()) {
                TablesDto dto = new TablesDto(
                        resultSet.getString("TableID"),
                        resultSet.getString("TableNumber"),
                        resultSet.getInt("Capacity"),
                        resultSet.getString("Location"),
                        resultSet.getString("Status")
                );
                tables.add(dto);
            }

            if (tables.isEmpty()) {
                System.out.println("No tables found with capacity >= " + capacity);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error fetching available tables: " + e.getMessage());
            throw e;
        }

        return tables;
    }


    public boolean updateTableStatus(String tableID, String newStatus) throws SQLException, ClassNotFoundException {
        if (tableID == null || tableID.trim().isEmpty()) {
            throw new IllegalArgumentException("TableID cannot be null or empty.");
        }
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        String sql = "UPDATE Tables SET Status = ? WHERE TableID = ?";

        try {
            return CrudUtil.execute(sql, newStatus, tableID);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating table status for TableID " + tableID + ": " + e.getMessage());
            throw e;
        }
    }




}