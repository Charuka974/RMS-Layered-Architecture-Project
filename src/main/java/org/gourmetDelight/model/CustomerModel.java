package org.gourmetDelight.model;

import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.util.CrudUtil;


import java.sql.*;
import java.util.ArrayList;

public class CustomerModel {


    public ArrayList<CustomerDto> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT CustomerID, Name, Phone, Email, Address, UserID FROM Customers";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<CustomerDto> customers = new ArrayList<>();

        while (resultSet.next()) {
            String customerID = resultSet.getString("CustomerID");
            String cusName = resultSet.getString("Name");
            String cusPhone = resultSet.getString("Phone");
            String cusEmail = resultSet.getString("Email");
            String cusAddress = resultSet.getString("Address");
            String cusUserID = resultSet.getString("UserID");

            CustomerDto customer = new CustomerDto(customerID, cusName, cusPhone, cusEmail, cusAddress, cusUserID);
            customers.add(customer);
        }

        resultSet.close();
        return customers;
    }



    public String saveCustomer(CustomerDto customerDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Customers (CustomerID, Name, Phone, Email, Address, UserID) VALUES (?, ?, ?, ?, ?, ?)";
        Boolean result = CrudUtil.execute(sql,
                customerDto.getCustomerID(),
                customerDto.getCusName(),
                customerDto.getCusPhone(),
                customerDto.getCusEmail(),
                customerDto.getCusAddress(),
                customerDto.getCusUserID()
        );
        return result ? "Successfully saved" : "Failed to save";
    }


    public String deleteCustomer(String customerId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";
        Boolean result = CrudUtil.execute(sql, customerId);
        return result ? "Successfully deleted" : "Failed to delete";
    }


    public String updateCustomer(CustomerDto customerDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Customers SET Name = ?, Phone = ?, Email = ?, Address = ? WHERE CustomerID = ?";
        Boolean result = CrudUtil.execute(sql,
                customerDto.getCusName(),
                customerDto.getCusPhone(),
                customerDto.getCusEmail(),
                customerDto.getCusAddress(),
                customerDto.getCustomerID()
        );
        return result ? "Successfully updated" : "Failed to update";
    }


    public CustomerDto searchCustomerById(String customerId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, customerId);

        if (resultSet.next()) {
            CustomerDto customer = new CustomerDto(
                    resultSet.getString("CustomerID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email"),
                    resultSet.getString("Address"),
                    resultSet.getString("UserID")
            );
            resultSet.close();
            return customer;
        } else {
            resultSet.close();
            return null;
        }
    }


    public ArrayList<CustomerDto> searchCustomersByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Customers WHERE Name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<CustomerDto> customerDtos = new ArrayList<>();

        while (resultSet.next()) {
            CustomerDto dto = new CustomerDto(
                    resultSet.getString("CustomerID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email"),
                    resultSet.getString("Address"),
                    resultSet.getString("UserID")
            );
            customerDtos.add(dto);
        }

        resultSet.close();
        return customerDtos;
    }

    public String suggestNextCustomerID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT CustomerID FROM Customers ORDER BY CustomerID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("C%03d", newIdIndex);
        }
        return "C001";
    }



}
