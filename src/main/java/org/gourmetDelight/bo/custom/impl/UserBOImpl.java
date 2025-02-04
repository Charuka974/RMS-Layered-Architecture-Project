package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.UserBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.UsersDAO;

import org.gourmetDelight.dto.employee.UserDto;

import org.gourmetDelight.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserBOImpl implements UserBO {
    UsersDAO userDAO = (UsersDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USERS);

    public ArrayList<UserDto> getAll() throws ClassNotFoundException, SQLException {
        ArrayList<User> users = userDAO.getAll();
        ArrayList<UserDto> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDto userDTO = new UserDto();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setLoginTime(user.getLoginTime());
            userDTO.setEmployeeID(user.getEmployeeID());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }


    public boolean save(UserDto dto) throws ClassNotFoundException, SQLException {
        User user = new User(dto.getUserId(),dto.getUsername(),dto.getPassword(),dto.getLoginTime(),dto.getEmployeeID());
        return userDAO.save(user);
    }

    public boolean delete(String id) throws ClassNotFoundException, SQLException {
        return userDAO.delete(id);
    }

    public boolean update(UserDto dto) throws ClassNotFoundException, SQLException {
        User user = new User(dto.getUserId(),dto.getUsername(),dto.getPassword(),dto.getLoginTime(),dto.getEmployeeID());
        return userDAO.update(user);
    }

    public UserDto searchById(String id) throws ClassNotFoundException, SQLException {
        User user = userDAO.searchById(id);
        UserDto userDTO = new UserDto();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setLoginTime(user.getLoginTime());
        userDTO.setEmployeeID(user.getEmployeeID());
        return userDTO;
    }

    public ArrayList<UserDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        ArrayList<User> users = userDAO.searchByName(name);
        ArrayList<UserDto> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDto userDTO = new UserDto();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setLoginTime(user.getLoginTime());
            userDTO.setEmployeeID(user.getEmployeeID());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return userDAO.suggestNextID();
    }



    public ArrayList<String> searchToHidePassword() throws ClassNotFoundException, SQLException {
        return userDAO.searchToHidePassword();
    }


    public boolean validateUserForgetPassword(String username) throws SQLException, ClassNotFoundException {
        return userDAO.validateUserForgetPassword(username);
    }

    public void setNewPassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        userDAO.setNewPassword(username,newPassword);
    }


    public boolean validateUser(String username, String password) throws ClassNotFoundException, SQLException {
        return userDAO.validateUser(username,password);
    }

    public String getUserID(String username, String password) throws ClassNotFoundException, SQLException {
        return userDAO.getUserID(username,password);
    }

}
