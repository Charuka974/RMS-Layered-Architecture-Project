package org.gourmetDelight.dao.custom;


import javafx.scene.control.Alert;
import org.gourmetDelight.dao.CrudDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;

public interface ReminderDAO extends CrudDAO {

    public void checkAndPrintReminder();

    public void sendInventoryEmail() throws SQLException, ClassNotFoundException;

    public void updateStartingDate(String reminderID, LocalDateTime newStartingDate);

    public void showAlert(Alert.AlertType alertType, String title, String header, String content);

}
