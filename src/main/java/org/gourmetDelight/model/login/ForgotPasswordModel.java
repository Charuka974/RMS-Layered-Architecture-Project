package org.gourmetDelight.model.login;

import org.gourmetDelight.db.DBConnection;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordModel {

        public boolean validateUser(String username) throws SQLException, ClassNotFoundException {
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


    public String selectEmail(String username) throws SQLException, ClassNotFoundException  {

        String email = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Email " +
                "FROM Users u " +
                "JOIN Employees e ON u.EmployeeID = e.EmployeeID " +
                "WHERE u.Username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    email = result.getString("Email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }

    public String selectPhone(String username) throws SQLException, ClassNotFoundException {
        String phoneNumber = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Phone " +
                "FROM Users u " +
                "JOIN Employees e ON u.EmployeeID = e.EmployeeID " +
                "WHERE u.Username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    phoneNumber = result.getString("Phone");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phoneNumber;

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


}
