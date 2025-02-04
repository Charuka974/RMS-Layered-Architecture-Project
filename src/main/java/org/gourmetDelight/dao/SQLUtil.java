package org.gourmetDelight.dao;

import org.gourmetDelight.db.DBConnection;

import java.sql.*;

public class SQLUtil {
    public static <T>T execute(String sql, Object... obj) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        for(int i = 0 ; i < obj.length; i++){
            pst.setObject((i+1), obj[i]);
        }
        if (sql.startsWith("SELECT") | sql.startsWith("select")) {
            ResultSet resultSet = pst.executeQuery();
            return (T) resultSet;
        }
        else{
            int i = pst.executeUpdate();
            boolean isDone = i > 0;
            return (T) ((Boolean)isDone);
        }
    }
}