package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeDAO extends CrudDAO<Employee> {

    public String getEmployeeName(String EmployeeID) throws ClassNotFoundException, SQLException;
    public ArrayList<String> selectAllManagerEmails() throws SQLException, ClassNotFoundException;

}
