package org.gourmetDelight.bo.custom;

import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.employee.UserDto;
import org.gourmetDelight.entity.User;
import org.gourmetDelight.util.CrudUtil;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface UserBO {
    public ArrayList<UserDto> getAll() throws ClassNotFoundException, SQLException;

    public boolean save(UserDto userDto) throws ClassNotFoundException, SQLException;

    public boolean delete(String userId) throws ClassNotFoundException, SQLException;

    public boolean update(UserDto userDto) throws ClassNotFoundException, SQLException;

    public UserDto searchById(String userId) throws ClassNotFoundException, SQLException;

    public ArrayList<UserDto> searchByName(String username) throws ClassNotFoundException, SQLException;

    public String suggestNextID() throws ClassNotFoundException, SQLException;

    public ArrayList<String> searchToHidePassword() throws ClassNotFoundException, SQLException;

    public boolean validateUserForgetPassword(String username) throws SQLException, ClassNotFoundException;

    public void setNewPassword(String username, String newPassword) throws SQLException, ClassNotFoundException;

    public boolean validateUser(String username, String password) throws ClassNotFoundException, SQLException;

    public String getUserID(String username, String password) throws ClassNotFoundException, SQLException;

}
