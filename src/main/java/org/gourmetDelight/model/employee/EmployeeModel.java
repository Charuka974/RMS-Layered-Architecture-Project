package org.gourmetDelight.model.employee;


import org.gourmetDelight.dto.employee.EmployeeDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeModel {
    public static ArrayList<EmployeeDto> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT employeeID, name, position, phone, email, hireDate FROM employees";

        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<EmployeeDto> employees = new ArrayList<>();

        while (resultSet.next()) {
            String employeeID = resultSet.getString("employeeID");
            String name = resultSet.getString("name");
            String position = resultSet.getString("position");
            String phone = resultSet.getString("phone");
            String email = resultSet.getString("email");
            LocalDate hireDate = resultSet.getDate("hireDate").toLocalDate();

            EmployeeDto employee = new EmployeeDto(employeeID, name, position, phone, email, hireDate);
            employees.add(employee);
        }


        resultSet.close();
        return employees;
    }

    public String saveEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO employees (employeeID, name, position, phone, email, hireDate) VALUES (?, ?, ?, ?, ?, ?)";
        Boolean result = CrudUtil.execute(sql,
                employeeDto.getEmployeeID(),
                employeeDto.getName(),
                employeeDto.getPosition(),
                employeeDto.getPhone(),
                employeeDto.getEmail(),
                employeeDto.getHireDate()
        );
        return result ? "Successfully saved" : "Failed to save";
    }


    public String deleteEmployee(String employeeId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM employees WHERE employeeID = ?";
        Boolean result = CrudUtil.execute(sql, employeeId);
        return result ? "Successfully deleted" : "Failed to delete";
    }


    public String updateEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE employees SET name = ?, position = ?, phone = ?, email = ?, hireDate = ? WHERE employeeID = ?";
        Boolean result = CrudUtil.execute(sql,
                employeeDto.getName(),
                employeeDto.getPosition(),
                employeeDto.getPhone(),
                employeeDto.getEmail(),
                employeeDto.getHireDate(),
                employeeDto.getEmployeeID()
        );
        return result ? "Successfully updated" : "Failed to update";
    }


    public EmployeeDto searchEmployeeById(String employeeId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM employees WHERE employeeID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, employeeId);

        if (resultSet.next()) {
            EmployeeDto employee = new EmployeeDto(
                    resultSet.getString("employeeID"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getDate("hireDate").toLocalDate()
            );
            resultSet.close();
            return employee;
        } else {
            resultSet.close();
            return null;
        }
    }


    public ArrayList<EmployeeDto> searchEmployeesByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM employees WHERE name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<EmployeeDto> employeeDtos = new ArrayList<>();

        while (resultSet.next()) {
            EmployeeDto dto = new EmployeeDto(
                    resultSet.getString("employeeID"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getDate("hireDate").toLocalDate()
            );
            employeeDtos.add(dto);
        }

        resultSet.close();
        return employeeDtos;
    }

    public String suggestNextEmployeeID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT employeeID FROM employees ORDER BY employeeID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("E%03d", newIdIndex);
        }
        return "E001";
    }
}
