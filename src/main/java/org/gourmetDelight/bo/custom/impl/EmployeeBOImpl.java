package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.EmployeeBO;
import org.gourmetDelight.dao.custom.EmployeeDAO;
import org.gourmetDelight.dao.custom.QueryDAO;
import org.gourmetDelight.dao.custom.impl.QueryDAOImpl;
import org.gourmetDelight.dao.custom.impl.employee.EmployeeDAOImpl;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.employee.EmployeeDto;
import org.gourmetDelight.entity.Employee;
import org.gourmetDelight.util.CrudUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeBOImpl implements EmployeeBO {
    EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    QueryDAO queryDAO = new QueryDAOImpl();
    public ArrayList<EmployeeDto> getAll() throws ClassNotFoundException, SQLException {
        ArrayList<Employee> employees = employeeDAO.getAll();
        ArrayList<EmployeeDto> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDto employeeDTO = new EmployeeDto();
            employeeDTO.setEmployeeID(employee.getEmployeeID());
            employeeDTO.setName(employee.getName());
            employeeDTO.setPosition(employee.getPosition());
            employeeDTO.setPhone(employee.getPhone());
            employeeDTO.setEmail(employee.getEmail());
            employeeDTO.setHireDate(employee.getHireDate());
            employeeDTOs.add(employeeDTO);
        }
        return employeeDTOs;
    }



    public boolean save(EmployeeDto dto) throws ClassNotFoundException, SQLException {
        Employee employee = new Employee(dto.getEmployeeID(),dto.getName(),dto.getPosition(),dto.getPhone(),dto.getEmail(),dto.getHireDate());
        return employeeDAO.save(employee);
    }


    public boolean delete(String id) throws ClassNotFoundException, SQLException {
        return employeeDAO.delete(id);
    }


    public boolean update(EmployeeDto dto) throws ClassNotFoundException, SQLException {
        Employee employee = new Employee(dto.getEmployeeID(),dto.getName(),dto.getPosition(),dto.getPhone(),dto.getEmail(),dto.getHireDate());
        return employeeDAO.update(employee);
    }


    public EmployeeDto searchById(String id) throws ClassNotFoundException, SQLException {
        Employee employee = employeeDAO.searchById(id);
        EmployeeDto employeeDTO = new EmployeeDto();
        employeeDTO.setEmployeeID(employee.getEmployeeID());
        employeeDTO.setName(employee.getName());
        employeeDTO.setPosition(employee.getPosition());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setHireDate(employee.getHireDate());
        return employeeDTO;
    }


    public ArrayList<EmployeeDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        ArrayList<Employee> employees = employeeDAO.searchByName(name);
        ArrayList<EmployeeDto> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDto employeeDTO = new EmployeeDto();
            employeeDTO.setEmployeeID(employee.getEmployeeID());
            employeeDTO.setName(employee.getName());
            employeeDTO.setPosition(employee.getPosition());
            employeeDTO.setPhone(employee.getPhone());
            employeeDTO.setEmail(employee.getEmail());
            employeeDTO.setHireDate(employee.getHireDate());
            employeeDTOs.add(employeeDTO);
        }
        return employeeDTOs;
    }

    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return employeeDAO.suggestNextID();
    }


    public String getEmployeeName(String id) throws ClassNotFoundException, SQLException {
        return employeeDAO.getEmployeeName(id);
    }

    public String selectEmail(String username) throws SQLException, ClassNotFoundException  {
        return queryDAO.selectEmail(username);
    }

    public String selectPhone(String username) throws SQLException, ClassNotFoundException {
        return queryDAO.selectPhone(username);
    }

    public String getRole(String username, String password) throws ClassNotFoundException, SQLException{
        return queryDAO.getRole(username, password);
    }

}
