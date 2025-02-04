package org.gourmetDelight.dao.custom.impl;

import org.gourmetDelight.dao.custom.CustomerDAO;
import org.gourmetDelight.entity.Customer;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {


    public ArrayList<Customer> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT CustomerID, Name, Phone, Email, Address, UserID FROM Customers";
        ResultSet resultSet = SQLUtil.execute(sql);

        ArrayList<Customer> customers = new ArrayList<>();

        while (resultSet.next()) {
            String customerID = resultSet.getString("CustomerID");
            String cusName = resultSet.getString("Name");
            String cusPhone = resultSet.getString("Phone");
            String cusEmail = resultSet.getString("Email");
            String cusAddress = resultSet.getString("Address");
            String cusUserID = resultSet.getString("UserID");

            Customer customer = new Customer(customerID, cusName, cusPhone, cusEmail, cusAddress, cusUserID);
            customers.add(customer);
        }

        resultSet.close();
        return customers;
    }

    @Override
    public boolean save(Customer customerDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Customers (CustomerID, Name, Phone, Email, Address, UserID) VALUES (?, ?, ?, ?, ?, ?)";
        Boolean result = SQLUtil.execute(sql,
                customerDto.getCustomerID(),
                customerDto.getCusName(),
                customerDto.getCusPhone(),
                customerDto.getCusEmail(),
                customerDto.getCusAddress(),
                customerDto.getCusUserID()
        );
        return result;
    }


    public boolean delete(String customerId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";
        Boolean result = SQLUtil.execute(sql, customerId);
        return result;
    }

    @Override
    public boolean update(Customer customerDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Customers SET Name = ?, Phone = ?, Email = ?, Address = ? WHERE CustomerID = ?";
        Boolean result = SQLUtil.execute(sql,
                customerDto.getCusName(),
                customerDto.getCusPhone(),
                customerDto.getCusEmail(),
                customerDto.getCusAddress(),
                customerDto.getCustomerID()
        );
        return result;
    }


    public Customer searchById(String customerId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, customerId);

        if (resultSet.next()) {
            Customer customer = new Customer(
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


    public ArrayList<Customer> searchByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Customers WHERE Name LIKE ?";
        ResultSet resultSet = SQLUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<Customer> customerDtos = new ArrayList<>();

        while (resultSet.next()) {
            Customer dto = new Customer(
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

    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.execute("SELECT CustomerID FROM Customers ORDER BY CustomerID DESC LIMIT 1");

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
