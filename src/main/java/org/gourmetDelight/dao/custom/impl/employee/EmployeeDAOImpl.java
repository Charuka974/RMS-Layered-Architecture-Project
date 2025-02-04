package org.gourmetDelight.dao.custom.impl.employee;


import org.gourmetDelight.dao.custom.EmployeeDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.Employee;
import org.gourmetDelight.dao.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeDAOImpl implements EmployeeDAO {
    public ArrayList<Employee> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT employeeID, name, position, phone, email, hireDate FROM employees";

        ResultSet resultSet = SQLUtil.execute(sql);

        ArrayList<Employee> employees = new ArrayList<>();

        while (resultSet.next()) {
            String employeeID = resultSet.getString("employeeID");
            String name = resultSet.getString("name");
            String position = resultSet.getString("position");
            String phone = resultSet.getString("phone");
            String email = resultSet.getString("email");
            LocalDate hireDate = resultSet.getDate("hireDate").toLocalDate();

            Employee employee = new Employee(employeeID, name, position, phone, email, hireDate);
            employees.add(employee);
        }


        resultSet.close();
        return employees;
    }

    public boolean save(Employee employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO employees (employeeID, name, position, phone, email, hireDate) VALUES (?, ?, ?, ?, ?, ?)";
        Boolean result = SQLUtil.execute(sql,
                employeeDto.getEmployeeID(),
                employeeDto.getName(),
                employeeDto.getPosition(),
                employeeDto.getPhone(),
                employeeDto.getEmail(),
                employeeDto.getHireDate()
        );
        return result;
    }


    public boolean delete(String employeeId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM employees WHERE employeeID = ?";
        Boolean result = SQLUtil.execute(sql, employeeId);
        return result;
    }


    public boolean update(Employee employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE employees SET name = ?, position = ?, phone = ?, email = ?, hireDate = ? WHERE employeeID = ?";
        Boolean result = SQLUtil.execute(sql,
                employeeDto.getName(),
                employeeDto.getPosition(),
                employeeDto.getPhone(),
                employeeDto.getEmail(),
                employeeDto.getHireDate(),
                employeeDto.getEmployeeID()
        );
        return result;
    }


    public Employee searchById(String employeeId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM employees WHERE employeeID = ?";
        ResultSet resultSet = SQLUtil.execute(sql, employeeId);

        if (resultSet.next()) {
            Employee employee = new Employee(
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


    public ArrayList<Employee> searchByName(String name) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM employees WHERE name LIKE ?";
        ResultSet resultSet = SQLUtil.execute(sql, "%" + name.toLowerCase() + "%");

        ArrayList<Employee> employeeDtos = new ArrayList<>();

        while (resultSet.next()) {
            Employee dto = new Employee(
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

    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.execute("SELECT employeeID FROM employees ORDER BY employeeID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("E%03d", newIdIndex);
        }
        return "E001";
    }


    public String getEmployeeName(String EmployeeID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT name FROM employees WHERE employeeID LIKE ?";
        ResultSet resultSet = SQLUtil.execute(sql, "%" + EmployeeID + "%");

        String Name = null;

        while (resultSet.next()) {
            Name = resultSet.getString("name");

        }

        resultSet.close();
        return Name;
    }

    public ArrayList<String> selectAllManagerEmails() throws SQLException, ClassNotFoundException {

        ArrayList<String> emails = new ArrayList<>();

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Email " +
                "FROM Employees e " +
                "WHERE e.Position = 'Manager'";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                emails.add(result.getString("Email"));
                //EmailUtil.sendEmail(result.getString("Email"),"Gourmet Delight Inventory Reminder", "Please Check The Inventory and Update The Inventory");
                System.out.println("Inventory Email Sent");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emails;
    }


}


