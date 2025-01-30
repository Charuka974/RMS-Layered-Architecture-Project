package org.gourmetDelight.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO <T> extends SuperDAO{
    public ArrayList<T> getAll() throws ClassNotFoundException, SQLException;

    public boolean save(T dto) throws ClassNotFoundException, SQLException;

    public boolean delete(String Id) throws ClassNotFoundException, SQLException;

    public boolean update(T dto) throws ClassNotFoundException, SQLException;

    public T searchById(String Id) throws ClassNotFoundException, SQLException;

    public ArrayList<T> searchByName(String name) throws ClassNotFoundException, SQLException;

    public String suggestNextID() throws ClassNotFoundException, SQLException;

}
