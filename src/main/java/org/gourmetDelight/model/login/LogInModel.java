package org.gourmetDelight.model.login;
import org.gourmetDelight.db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LogInModel {
    public String getRole(String username, String password) throws ClassNotFoundException, SQLException {
        String role = null;

        java.sql.Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT e.Position " +
                "FROM Users u " +
                "JOIN Employees e ON u.EmployeeID = e.EmployeeID " +
                "WHERE u.Username = ? AND u.Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    role = result.getString("Position");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
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
