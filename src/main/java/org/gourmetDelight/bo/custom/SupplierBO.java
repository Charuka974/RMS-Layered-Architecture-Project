package org.gourmetDelight.bo.custom;

import org.gourmetDelight.dto.inventory.SupplierDto;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierBO {

    public ArrayList<SupplierDto> getAll() throws ClassNotFoundException, SQLException;


    public boolean save(SupplierDto supplierDto) throws ClassNotFoundException, SQLException;

    public boolean delete(String supplierID) throws ClassNotFoundException, SQLException;
    public boolean update(SupplierDto supplierDto) throws ClassNotFoundException, SQLException;

    public SupplierDto searchById(String supplierID) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierDto> searchByName(String name) throws ClassNotFoundException, SQLException;
    public String suggestNextID() throws ClassNotFoundException, SQLException;

}
