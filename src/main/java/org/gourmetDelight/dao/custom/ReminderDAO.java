package org.gourmetDelight.dao.custom;


import javafx.scene.control.Alert;
import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.util.CrudUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public interface ReminderDAO extends CrudDAO {

    public void checkAndPrintReminder();

    private void sendInventoryEmail() throws SQLException, ClassNotFoundException{}

    private void updateStartingDate(String reminderID, LocalDateTime newStartingDate){}

    public ArrayList<String> selectAllManagerEmails() throws SQLException, ClassNotFoundException;

    public void showAlert(Alert.AlertType alertType, String title, String header, String content);

}
