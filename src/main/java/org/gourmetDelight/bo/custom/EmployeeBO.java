package org.gourmetDelight.bo.custom;

import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.employee.EmployeeDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeBO extends SuperBO {
    public ArrayList<EmployeeDto> getAll() throws ClassNotFoundException, SQLException;

    public boolean save(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException;

    public boolean delete(String employeeId) throws ClassNotFoundException, SQLException;

    public boolean update(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException;

    public EmployeeDto searchById(String employeeId) throws ClassNotFoundException, SQLException;

    public ArrayList<EmployeeDto> searchByName(String name) throws ClassNotFoundException, SQLException;

    public String suggestNextID() throws ClassNotFoundException, SQLException;

    public String getEmployeeName(String id) throws ClassNotFoundException, SQLException;

    public String selectEmail(String username) throws SQLException, ClassNotFoundException;

    public String selectPhone(String username) throws SQLException, ClassNotFoundException;

    public String getRole(String username, String password) throws ClassNotFoundException, SQLException;

}
