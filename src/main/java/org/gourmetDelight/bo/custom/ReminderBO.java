package org.gourmetDelight.bo.custom;

import javafx.scene.control.Alert;
import org.gourmetDelight.bo.SuperBO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public interface ReminderBO extends SuperBO {

    public void checkAndPrintReminder();

    public void sendInventoryEmail() throws SQLException, ClassNotFoundException;


    public void updateStartingDate(String reminderID, LocalDateTime newStartingDate);

    public ArrayList getAll() throws ClassNotFoundException, SQLException;
    public boolean save(Object dto) throws ClassNotFoundException, SQLException;
    public boolean delete(String Id) throws ClassNotFoundException, SQLException;
    public boolean update(Object dto) throws ClassNotFoundException, SQLException;
    public Object searchById(String Id) throws ClassNotFoundException, SQLException;
    public ArrayList searchByName(String name) throws ClassNotFoundException, SQLException;
    public String suggestNextID() throws ClassNotFoundException, SQLException;

}
