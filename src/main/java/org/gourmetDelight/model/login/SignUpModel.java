package org.gourmetDelight.model.login;

import org.gourmetDelight.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignUpModel {
    public String setEmployeeId() throws Exception{
        Connection connection= DBConnection.getInstance().getConnection();
        String sql="select EmployeeID from Employees order by EmployeeID desc limit 1";
        PreparedStatement statement=connection.prepareStatement(sql);
        ResultSet rst= statement.executeQuery();
        String id="";
        while(rst.next()){
            id=rst.getString("EmployeeID").replaceAll("[^0-9]", "");
        }
        return id;
    }

    public String setUserId() throws Exception{
        Connection connection=DBConnection.getInstance().getConnection();
        String sql="select UserID from Users order by UserID desc limit 1";
        PreparedStatement statement=connection.prepareStatement(sql);
        ResultSet rst= statement.executeQuery();
        String id="";
        while(rst.next()){
            id=rst.getString("UserID").replaceAll("[^0-9]", "");
        }
        return id;
    }
}
