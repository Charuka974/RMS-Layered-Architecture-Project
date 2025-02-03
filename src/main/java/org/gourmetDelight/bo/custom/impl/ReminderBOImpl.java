package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.ReminderBO;
import org.gourmetDelight.dao.custom.ReminderDAO;
import org.gourmetDelight.dao.custom.impl.home.ReminderDAOImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReminderBOImpl implements ReminderBO {
    ReminderDAO reminderDAO = new ReminderDAOImpl();

    public void checkAndPrintReminder() {
        reminderDAO.checkAndPrintReminder();
    }

    public void sendInventoryEmail() throws SQLException, ClassNotFoundException {
        reminderDAO.sendInventoryEmail();
    }


    public void updateStartingDate(String reminderID, LocalDateTime newStartingDate) {
        reminderDAO.updateStartingDate(reminderID, newStartingDate);
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
