package org.gourmetDelight.model.inventory;

import org.gourmetDelight.dto.inventory.SupplierDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {

    // Method to get all suppliers
    public ArrayList<SupplierDto> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT SupplierID, Name, ContactPerson, Phone, Email, Address, UserID FROM Suppliers";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<SupplierDto> suppliers = new ArrayList<>();

        while (resultSet.next()) {
            String supplierID = resultSet.getString("SupplierID");
            String name = resultSet.getString("Name");
            String contactPerson = resultSet.getString("ContactPerson");
            String phone = resultSet.getString("Phone");
            String email = resultSet.getString("Email");
            String address = resultSet.getString("Address");
            String userID = resultSet.getString("UserID");

            SupplierDto supplier = new SupplierDto(supplierID, name, contactPerson, phone, email, address, userID);
            suppliers.add(supplier);
        }

        resultSet.close();
        return suppliers;
    }

    // Method to save a supplier
    public String saveSupplier(SupplierDto supplierDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Suppliers (SupplierID, Name, ContactPerson, Phone, Email, Address, UserID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Boolean result = CrudUtil.execute(sql,
                supplierDto.getSupplierID(),
                supplierDto.getName(),
                supplierDto.getContactPerson(),
                supplierDto.getPhone(),
                supplierDto.getEmail(),
                supplierDto.getAddress(),
                supplierDto.getUserID()
        );
        return result ? "Successfully saved" : "Failed to save";
    }

    // Method to delete a supplier by ID
    public String deleteSupplier(String supplierID) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Suppliers WHERE SupplierID = ?";
        Boolean result = CrudUtil.execute(sql, supplierID);
        return result ? "Successfully deleted" : "Failed to delete";
    }

    // Method to update supplier details
    public String updateSupplier(SupplierDto supplierDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Suppliers SET Name = ?, ContactPerson = ?, Phone = ?, Email = ?, Address = ? WHERE SupplierID = ?";
        Boolean result = CrudUtil.execute(sql,
                supplierDto.getName(),
                supplierDto.getContactPerson(),
                supplierDto.getPhone(),
                supplierDto.getEmail(),
                supplierDto.getAddress(),
                supplierDto.getSupplierID()
        );
        return result ? "Successfully updated" : "Failed to update";
    }

    // Method to search for a supplier by ID
    public SupplierDto searchSupplierById(String supplierID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Suppliers WHERE SupplierID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, supplierID);

        if (resultSet.next()) {
            SupplierDto supplier = new SupplierDto(
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
    public ArrayList<SupplierDto> searchSuppliersByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Suppliers WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<SupplierDto> supplierDtos = new ArrayList<>();

        while (resultSet.next()) {
            SupplierDto dto = new SupplierDto(
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
    public String suggestNextSupplierID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT SupplierID FROM Suppliers ORDER BY SupplierID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int newIdIndex = Integer.parseInt(substring) + 1;
            return String.format("S%03d", newIdIndex);
        }
        return "S001";
    }
}
