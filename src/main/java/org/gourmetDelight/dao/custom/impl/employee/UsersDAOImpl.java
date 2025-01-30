package org.gourmetDelight.dao.custom.impl.employee;

import org.gourmetDelight.dao.custom.UsersDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.User;
import org.gourmetDelight.util.CrudUtil;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class UsersDAOImpl implements UsersDAO {

    public ArrayList<User> getAll() throws ClassNotFoundException, SQLException {
        String sql = "SELECT UserID, Username, Password, LoginTime, EmployeeID FROM Users";

        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<User> users = new ArrayList<>();

        while (resultSet.next()) {
            String userId = resultSet.getString("UserID");
            String username = resultSet.getString("Username");
            String password = resultSet.getString("Password");
            LocalDate loginTime = resultSet.getTimestamp("LoginTime").toLocalDateTime().toLocalDate();
            String employeeID = resultSet.getString("EmployeeID");

            User user = new User(userId, username, password, loginTime, employeeID);
            users.add(user);
        }

        resultSet.close();
        return users;
    }

    public boolean save(User userDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID) VALUES (?, ?, ?, ?, ?)";
        Boolean result = CrudUtil.execute(sql,
                userDto.getUserId(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getLoginTime(),
                userDto.getEmployeeID()
        );
        return result;
    }

    public boolean delete(String userId) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        Boolean result = CrudUtil.execute(sql, userId);
        return result;
    }

    public boolean update(User userDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Users SET Username = ?, Password = ?, LoginTime = ?, EmployeeID = ? WHERE UserID = ?";
        Boolean result = CrudUtil.execute(sql,
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getLoginTime(),
                userDto.getEmployeeID(),
                userDto.getUserId()
        );
        return result;
    }

    public User searchById(String userId) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        ResultSet resultSet = CrudUtil.execute(sql, userId);

        if (resultSet.next()) {
            User user = new User(
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

    public ArrayList<User> searchByName(String username) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Users WHERE Username LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, "%" + username + "%");

        ArrayList<User> userDtos = new ArrayList<>();

        while (resultSet.next()) {
            User dto = new User(
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

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
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



    public ArrayList<String> searchToHidePassword() throws ClassNotFoundException, SQLException {
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


    public boolean validateUserForgetPassword(String username) throws SQLException, ClassNotFoundException {
        boolean userExists = false;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();

        // check if the username exists
        String sql = "SELECT EXISTS (SELECT 1 FROM Users WHERE Username = ?) AS ValueExists";


        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);


            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    userExists = result.getBoolean("ValueExists");
                    //userExists = true;

                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return userExists;
    }

    public void setNewPassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE Users SET Password = ? WHERE Username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newPassword);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Username not found or password not updated.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }


    public boolean validateUser(String username, String password) throws ClassNotFoundException, SQLException {
        boolean isValid = false;


        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);


            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    isValid = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public String getUserID(String username, String password) throws ClassNotFoundException, SQLException {
        String userID = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT UserID FROM Users WHERE Username = ? AND Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    userID = result.getString("UserID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userID;
    }





}
