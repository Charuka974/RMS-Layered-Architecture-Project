package org.gourmetDelight.dao.custom;


import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.Employee;
import org.gourmetDelight.util.CrudUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeDAO extends CrudDAO<Employee> {

    public String getEmployeeName(String EmployeeID) throws ClassNotFoundException, SQLException;
    public ArrayList<String> selectAllManagerEmails() throws SQLException, ClassNotFoundException;

}
