package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.CrudDAO;
import org.gourmetDelight.entity.TableAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TableAssignmentsDAO extends CrudDAO<TableAssignments> {
    public ResultSet searchByReserveId(String Id) throws ClassNotFoundException, SQLException;

}
