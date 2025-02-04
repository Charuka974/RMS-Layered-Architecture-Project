package org.gourmetDelight.dao.custom.impl.home;

import javafx.scene.control.Alert;
import org.gourmetDelight.dao.custom.EmployeeDAO;
import org.gourmetDelight.dao.custom.ReminderDAO;
import org.gourmetDelight.dao.custom.impl.employee.EmployeeDAOImpl;
import org.gourmetDelight.dao.SQLUtil;
import org.gourmetDelight.util.DateAndTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReminderDAOImpl implements ReminderDAO {

    DateAndTime dateAndTime = new DateAndTime();
    EmployeeDAO employeeDAO = new EmployeeDAOImpl();

    // ------------Check Time-------------------
    public void checkAndPrintReminder() {
        String reminderID = "RT001";

        String sql = "SELECT StartingDate, Duration FROM Reminder WHERE ReminderID = ?";

        try {
            ResultSet resultSet = SQLUtil.execute(sql, reminderID);

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

    public void sendInventoryEmail() throws SQLException, ClassNotFoundException {
        employeeDAO.selectAllManagerEmails();

    }


    public void updateStartingDate(String reminderID, LocalDateTime newStartingDate) {
        String updateSql = "UPDATE Reminder SET StartingDate = ? WHERE ReminderID = ?";

        try {
            // Format the new StartingDate as a string
            String formattedDate = newStartingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            boolean isUpdated = SQLUtil.execute(updateSql, formattedDate, reminderID);

            if (isUpdated) {
                System.out.println("\nStarting date updated to: " + formattedDate);
            } else {
                System.out.println("Failed to update StartingDate.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public ArrayList getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Object dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean delete(String Id) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public boolean update(Object dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public Object searchById(String Id) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return "";
    }
}
