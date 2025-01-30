package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.employee.UserDto;
import org.gourmetDelight.entity.User;
import org.gourmetDelight.util.CrudUtil;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface UsersDAO extends CrudDAO<User> {

    public ArrayList<String> searchToHidePassword() throws ClassNotFoundException, SQLException;

    public boolean validateUserForgetPassword(String username) throws SQLException, ClassNotFoundException;

    public void setNewPassword(String username, String newPassword) throws SQLException, ClassNotFoundException;

    public boolean validateUser(String username, String password) throws ClassNotFoundException, SQLException;

    public String getUserID(String username, String password) throws ClassNotFoundException, SQLException;
}
