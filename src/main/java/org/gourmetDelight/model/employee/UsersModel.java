package org.gourmetDelight.model.employee;

import org.gourmetDelight.dto.employee.UserDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class UsersModel {

    public static ArrayList<UserDto> getAllUsers() throws ClassNotFoundException, SQLException {
        String sql = "SELECT UserID, Username, Password, LoginTime, EmployeeID FROM Users";

        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<UserDto> users = new ArrayList<>();

        while (resultSet.next()) {
            String userId = resultSet.getString("UserID");
            String username = resultSet.getString("Username");
            String password = resultSet.getString("Password");
            LocalDate loginTime = resultSet.getTimestamp("LoginTime").toLocalDateTime().toLocalDate();
            String employeeID = resultSet.getString("EmployeeID");

            UserDto user = new UserDto(userId, username, password, loginTime, employeeID);
            users.add(user);
        }

        resultSet.close();
        return users;
    }

    public String saveUser(UserDto userDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID) VALUES (?, ?, ?, ?, ?)";
        Boolean result = CrudUtil.execute(sql,
                userDto.getUserId(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getLoginTime(),
                userDto.getEmployeeID()
        );
        return result ? "Successfully saved" : "Failed to save";
    }

    public String deleteUser(String userId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        Boolean result = CrudUtil.execute(sql, userId);
        return result ? "Successfully deleted" : "Failed to delete";
    }

    public String updateUser(UserDto userDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Users SET Username = ?, Password = ?, LoginTime = ?, EmployeeID = ? WHERE UserID = ?";
        Boolean result = CrudUtil.execute(sql,
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getLoginTime(),
                userDto.getEmployeeID(),
                userDto.getUserId()
        );
        return result ? "Successfully updated" : "Failed to update";
    }

    public UserDto searchUserById(String userId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userId);

        if (resultSet.next()) {
            UserDto user = new UserDto(
                    resultSet.getString("UserID"),
                    resultSet.getString("Username"),
                    resultSet.getString("Password"),
                    resultSet.getTimestamp("LoginTime").toLocalDateTime().toLocalDate(),
                    resultSet.getString("EmployeeID")
            );
            resultSet.close();
            return user;
        } else {
            resultSet.close();
            return null;
        }
    }

    public ArrayList<UserDto> searchUsersByUsername(String username) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Users WHERE Username LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + username + "%");

        ArrayList<UserDto> userDtos = new ArrayList<>();

        while (resultSet.next()) {
            UserDto dto = new UserDto(
                    resultSet.getString("UserID"),
                    resultSet.getString("Username"),
                    resultSet.getString("Password"),
                    resultSet.getTimestamp("LoginTime").toLocalDateTime().toLocalDate(),
                    resultSet.getString("EmployeeID")
            );
            userDtos.add(dto);
        }

        resultSet.close();
        return userDtos;
    }

    public String suggestNextUserId() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT UserID FROM Users ORDER BY UserID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("U%03d", newIdIndex);
        }
        return "U001";
    }

    public String searchEmployeeName(String username) throws ClassNotFoundException, SQLException {
        String sql = "SELECT employeeID FROM employees WHERE name LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + username + "%");

        String EmployeeID = null;

        while (resultSet.next()) {
            EmployeeID = resultSet.getString("employeeID");

        }

        resultSet.close();
        return EmployeeID;
    }

    public String getEmployeeName(String EmployeeID) throws ClassNotFoundException, SQLException {
        String sql = "SELECT name FROM employees WHERE employeeID LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + EmployeeID + "%");

        String Name = null;

        while (resultSet.next()) {
            Name = resultSet.getString("name");

        }

        resultSet.close();
        return Name;
    }

    public ArrayList<String> searchtoHidePassword() throws ClassNotFoundException, SQLException {
        String sql = "SELECT UserID FROM Users";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<String> users = new ArrayList<>();

        while (resultSet.next()) {
            String userId = resultSet.getString("UserID");

            String user = new String(userId);
            users.add(user);
        }

        resultSet.close();
        return users;
    }

}
