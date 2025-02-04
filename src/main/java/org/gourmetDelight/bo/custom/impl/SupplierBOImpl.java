package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.SupplierBO;
import org.gourmetDelight.dao.DAOFactory;
import org.gourmetDelight.dao.custom.CustomerDAO;
import org.gourmetDelight.dao.custom.SupplierDAO;
import org.gourmetDelight.dao.custom.impl.inventory.SupplierDAOImpl;
import org.gourmetDelight.dto.inventory.SupplierDto;
import org.gourmetDelight.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierBOImpl implements SupplierBO {
    SupplierDAO  supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SUPPLIER);

    public ArrayList<SupplierDto> getAll() throws ClassNotFoundException, SQLException {
        ArrayList<Supplier> suppliers = supplierDAO.getAll();
        ArrayList<SupplierDto> supplierDTOs = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            SupplierDto supplierDTO = new SupplierDto();
            supplierDTO.setSupplierID(supplier.getSupplierID());
            supplierDTO.setName(supplier.getName());
            supplierDTO.setContactPerson(supplier.getContactPerson());
            supplierDTO.setPhone(supplier.getPhone());
            supplierDTO.setEmail(supplier.getEmail());
            supplierDTO.setAddress(supplier.getAddress());
            supplierDTO.setUserID(supplier.getUserID());
            supplierDTOs.add(supplierDTO);
        }
        return supplierDTOs;
    }


    public boolean save(SupplierDto supplierDto) throws ClassNotFoundException, SQLException {
        return supplierDAO.save(new Supplier(
                supplierDto.getSupplierID(),
                supplierDto.getName(),
                supplierDto.getContactPerson(),
                supplierDto.getPhone(),
                supplierDto.getEmail(),
                supplierDto.getAddress(),
                supplierDto.getUserID()
        ));
    }

    public boolean delete(String supplierID) throws ClassNotFoundException, SQLException {
        return supplierDAO.delete(supplierID);
    }

    public boolean update(SupplierDto supplierDto) throws ClassNotFoundException, SQLException {
       return supplierDAO.update(new Supplier(
                supplierDto.getSupplierID(),
                supplierDto.getName(),
                supplierDto.getContactPerson(),
                supplierDto.getPhone(),
                supplierDto.getEmail(),
                supplierDto.getAddress(),
                supplierDto.getUserID()
        ));
    }

    public SupplierDto searchById(String supplierID) throws ClassNotFoundException, SQLException {
        Supplier supplier = supplierDAO.searchById(supplierID);
        SupplierDto supplierDTO = new SupplierDto();
        supplierDTO.setSupplierID(supplier.getSupplierID());
        supplierDTO.setName(supplier.getName());
        supplierDTO.setContactPerson(supplier.getContactPerson());
        supplierDTO.setPhone(supplier.getPhone());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setUserID(supplier.getUserID());

        return supplierDTO;
    }

    public ArrayList<SupplierDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        ArrayList<Supplier> suppliers = supplierDAO.searchByName(name);
        ArrayList<SupplierDto> supplierDTOs = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            SupplierDto supplierDTO = new SupplierDto();
            supplierDTO.setSupplierID(supplier.getSupplierID());
            supplierDTO.setName(supplier.getName());
            supplierDTO.setContactPerson(supplier.getContactPerson());
            supplierDTO.setPhone(supplier.getPhone());
            supplierDTO.setEmail(supplier.getEmail());
            supplierDTO.setAddress(supplier.getAddress());
            supplierDTO.setUserID(supplier.getUserID());
            supplierDTOs.add(supplierDTO);
        }
        return supplierDTOs;
    }


    public String suggestNextID() throws ClassNotFoundException, SQLException {
       return supplierDAO.suggestNextID();
    }


}
