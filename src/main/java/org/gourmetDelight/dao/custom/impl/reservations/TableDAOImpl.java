package org.gourmetDelight.dao.custom.impl.reservations;

import org.gourmetDelight.dao.custom.TableDAO;
import org.gourmetDelight.entity.Tables;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableDAOImpl implements TableDAO {

    @Override
    public ArrayList<Tables> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT TableID, TableNumber, Capacity, Location, Status FROM Tables";
        ResultSet resultSet = SQLUtil.execute(sql);

        ArrayList<Tables> tables = new ArrayList<>();

        while (resultSet.next()) {
            String tableID = resultSet.getString("TableID");
            String tableNumber = resultSet.getString("TableNumber");
            int capacity = resultSet.getInt("Capacity");
            String location = resultSet.getString("Location");
            String status = resultSet.getString("Status");

            Tables table = new Tables(tableID, tableNumber, capacity, location, status);
            tables.add(table);
        }

        resultSet.close();
        return tables;
    }


    @Override
    public boolean save(Tables tablesDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Tables (TableID, TableNumber, Capacity, Location, Status) VALUES (?, ?, ?, ?, ?)";
        boolean result = SQLUtil.execute(sql,
                tablesDto.getTableID(),
                tablesDto.getTableNumber(),
                tablesDto.getCapacity(),
                tablesDto.getLocation(),
                tablesDto.getStatus()
        );
        return result;
    }


    @Override
    public boolean delete(String tableID) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Tables WHERE TableID = ?";
        boolean result = SQLUtil.execute(sql, tableID);
        return result;
    }


    @Override
    public boolean update(Tables tablesDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Tables SET TableNumber = ?, Capacity = ?, Location = ?, Status = ? WHERE TableID = ?";
        boolean result = SQLUtil.execute(sql,
                tablesDto.getTableNumber(),
                tablesDto.getCapacity(),
                tablesDto.getLocation(),
                tablesDto.getStatus(),
                tablesDto.getTableID()
        );
        return result;
    }

    @Override
    public Tables searchById(String tableID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Tables WHERE TableID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, tableID);

        if (resultSet.next()) {
            Tables table = new Tables(
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

    @Override
    public ArrayList<Tables> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }


    public ArrayList<Tables> searchTablesByLocation(String location) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Tables WHERE Location LIKE ?";
        ResultSet resultSet = SQLUtil.execute(sql, "%" + location.toLowerCase() + "%");

        ArrayList<Tables> tables = new ArrayList<>();

        while (resultSet.next()) {
            Tables dto = new Tables(
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

    public ArrayList<Tables> searchTablesByAvailability(String status) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Tables WHERE Status = ?";
        ResultSet resultSet = SQLUtil.execute(sql, status);

        ArrayList<Tables> tables = new ArrayList<>();

        while (resultSet.next()) {
            Tables dto = new Tables(
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

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        String sql = "SELECT TableID FROM Tables ORDER BY TableID DESC LIMIT 1";
        ResultSet resultSet = SQLUtil.execute(sql);

        if (resultSet.next()) {
            String lastId = resultSet.getString("TableID");
            String idNumber = lastId.substring(1);
            int newIdIndex = Integer.parseInt(idNumber) + 1;
            return String.format("T%03d", newIdIndex);
        }
        return "T001";
    }



    public ArrayList<Tables> findAvailableTable(int capacity) throws ClassNotFoundException, SQLException {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }

        String sql = "SELECT * FROM Tables WHERE Status = ? AND Capacity >= ?";
        ArrayList<Tables> tables = new ArrayList<>();

        try (ResultSet resultSet = SQLUtil.execute(sql, "Available", capacity)) {
            while (resultSet.next()) {
                Tables dto = new Tables(
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
            return SQLUtil.execute(sql, newStatus, tableID);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating table status for TableID " + tableID + ": " + e.getMessage());
            throw e;
        }
    }




}