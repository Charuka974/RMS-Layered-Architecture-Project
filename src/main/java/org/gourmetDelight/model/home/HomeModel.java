package org.gourmetDelight.model.home;

import javafx.scene.control.Alert;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.model.login.ForgotPasswordModel;
import org.gourmetDelight.util.CrudUtil;
import org.gourmetDelight.util.DateAndTime;
import org.gourmetDelight.util.EmailUtil;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeModel {

    DateAndTime dateAndTime = new DateAndTime();

    // ------------Check Time-------------------
    public void checkAndPrintReminder() {
        String reminderID = "RT001";

        String sql = "SELECT StartingDate, Duration FROM Reminder WHERE ReminderID = ?";

        try {
            ResultSet resultSet = CrudUtil.execute(sql, reminderID);

            if (resultSet != null && resultSet.next()) {
                String startingDateString = resultSet.getString("StartingDate");
                int duration = resultSet.getInt("Duration");

                // Parse the StartingDate into a LocalDateTime object
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime startingDate = LocalDateTime.parse(startingDateString, formatter);

                // Get the time now
                LocalDateTime currentDateTime = LocalDateTime.now();

                // Check if the current time is after or equal to the starting time plus duration
                LocalDateTime nextReminderTime = startingDate.plusMinutes(duration);

                if (currentDateTime.isAfter(nextReminderTime)) {
                    // Show the reminder alert
                    showAlert(Alert.AlertType.WARNING, "Reminder", "Inventory Reminder", "Please Check the Inventory and Update The Inventory");
                    sendInventoryEmail();
                    // Update the StartingDate to the current date and time
                    updateStartingDate(reminderID, currentDateTime);
                }
            } else {
                System.out.println("No reminder found for the given ReminderID.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendInventoryEmail() throws SQLException, ClassNotFoundException {
        selectAllManagerEmails();


    }


    private void updateStartingDate(String reminderID, LocalDateTime newStartingDate) {
        String updateSql = "UPDATE Reminder SET StartingDate = ? WHERE ReminderID = ?";

        try {
            // Format the new StartingDate as a string
            String formattedDate = newStartingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            boolean isUpdated = CrudUtil.execute(updateSql, formattedDate, reminderID);

            if (isUpdated) {
                System.out.println("\nStarting date updated to: " + formattedDate);
            } else {
                System.out.println("Failed to update StartingDate.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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




    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
