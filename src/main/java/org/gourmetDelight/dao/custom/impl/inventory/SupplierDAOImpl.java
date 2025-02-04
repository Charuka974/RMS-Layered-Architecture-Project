package org.gourmetDelight.dao.custom.impl.inventory;

import org.gourmetDelight.dao.custom.SupplierDAO;
import org.gourmetDelight.entity.Supplier;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAOImpl implements SupplierDAO {

    // Method to get all suppliers
    public ArrayList<Supplier> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT SupplierID, Name, ContactPerson, Phone, Email, Address, UserID FROM Suppliers";
        ResultSet resultSet = SQLUtil.execute(sql);

        ArrayList<Supplier> suppliers = new ArrayList<>();

        while (resultSet.next()) {
            String supplierID = resultSet.getString("SupplierID");
            String name = resultSet.getString("Name");
            String contactPerson = resultSet.getString("ContactPerson");
            String phone = resultSet.getString("Phone");
            String email = resultSet.getString("Email");
            String address = resultSet.getString("Address");
            String userID = resultSet.getString("UserID");

            Supplier supplier = new Supplier(supplierID, name, contactPerson, phone, email, address, userID);
            suppliers.add(supplier);
        }

        resultSet.close();
        return suppliers;
    }

    // Method to save a supplier
    public boolean save(Supplier supplierDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Suppliers (SupplierID, Name, ContactPerson, Phone, Email, Address, UserID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Boolean result = SQLUtil.execute(sql,
                supplierDto.getSupplierID(),
                supplierDto.getName(),
                supplierDto.getContactPerson(),
                supplierDto.getPhone(),
                supplierDto.getEmail(),
                supplierDto.getAddress(),
                supplierDto.getUserID()
        );
        return result;
    }

    // Method to delete a supplier by ID
    public boolean delete(String supplierID) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Suppliers WHERE SupplierID = ?";
        Boolean result = SQLUtil.execute(sql, supplierID);
        return result;
    }

    // Method to update supplier details
    public boolean update(Supplier supplierDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Suppliers SET Name = ?, ContactPerson = ?, Phone = ?, Email = ?, Address = ? WHERE SupplierID = ?";
        Boolean result = SQLUtil.execute(sql,
                supplierDto.getName(),
                supplierDto.getContactPerson(),
                supplierDto.getPhone(),
                supplierDto.getEmail(),
                supplierDto.getAddress(),
                supplierDto.getSupplierID()
        );
        return result;
    }

    // Method to search for a supplier by ID
    public Supplier searchById(String supplierID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Suppliers WHERE SupplierID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, supplierID);

        if (resultSet.next()) {
            Supplier supplier = new Supplier(
                    resultSet.getString("SupplierID"),
                    resultSet.getString("Name"),
                    resultSet.getString("ContactPerson"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email"),
                    resultSet.getString("Address"),
                    resultSet.getString("UserID")
            );
            resultSet.close();
            return supplier;
        } else {
            resultSet.close();
            return null;
        }
    }

    // Method to search suppliers by name
    public ArrayList<Supplier> searchByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Suppliers WHERE Name LIKE ?";
        ResultSet resultSet = SQLUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<Supplier> supplierDtos = new ArrayList<>();

        while (resultSet.next()) {
            Supplier dto = new Supplier(
                    resultSet.getString("SupplierID"),
                    resultSet.getString("Name"),
                    resultSet.getString("ContactPerson"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email"),
                    resultSet.getString("Address"),
                    resultSet.getString("UserID")
            );
            supplierDtos.add(dto);
        }

        resultSet.close();
        return supplierDtos;
    }

    // Method to suggest the next SupplierID
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.execute("SELECT SupplierID FROM Suppliers ORDER BY SupplierID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int newIdIndex = Integer.parseInt(substring) + 1;
            return String.format("S%03d", newIdIndex);
        }
        return "S001";
    }



}
