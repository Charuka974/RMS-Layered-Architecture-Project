package org.gourmetDelight.bo.custom;

import org.gourmetDelight.bo.SuperBO;
import org.gourmetDelight.dto.CustomerDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    public ArrayList<CustomerDto> getAll() throws ClassNotFoundException, SQLException;

    public boolean save(CustomerDto customerDto) throws ClassNotFoundException, SQLException;

    public boolean delete(String customerId) throws ClassNotFoundException, SQLException;

    public boolean update(CustomerDto customerDto) throws ClassNotFoundException, SQLException;

    public CustomerDto searchById(String customerId) throws ClassNotFoundException, SQLException;

    public ArrayList<CustomerDto> searchByName(String name) throws ClassNotFoundException, SQLException;

    public String suggestNextID() throws ClassNotFoundException, SQLException;

}
