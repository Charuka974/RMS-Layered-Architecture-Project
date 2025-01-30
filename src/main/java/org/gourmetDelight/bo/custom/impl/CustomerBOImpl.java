package org.gourmetDelight.bo.custom.impl;

import org.gourmetDelight.bo.custom.CustomerBO;
import org.gourmetDelight.dao.custom.CustomerDAO;
import org.gourmetDelight.dao.custom.impl.CustomerDAOImpl;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.entity.Customer;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO = new CustomerDAOImpl();
    public ArrayList<CustomerDto> getAll() throws ClassNotFoundException, SQLException {
        ArrayList<Customer> customers = customerDAO.getAll();
        ArrayList<CustomerDto> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDto customerDTO = new CustomerDto();
            customerDTO.setCustomerID(customer.getCustomerID());
            customerDTO.setCusName(customer.getCusName());
            customerDTO.setCusPhone(customer.getCusPhone());
            customerDTO.setCusEmail(customer.getCusEmail());
            customerDTO.setCusAddress(customer.getCusAddress());
            customerDTO.setCusUserID(customer.getCusUserID());
            customerDTOs.add(customerDTO);
        }
        return customerDTOs;
    }


    public boolean save(CustomerDto dto) throws ClassNotFoundException, SQLException {
        Customer customer = new Customer(dto.getCustomerID(),dto.getCusName(),dto.getCusPhone(),dto.getCusEmail(),dto.getCusAddress(),dto.getCusUserID());
        return customerDAO.save(customer);
    }


    public boolean delete(String id) throws ClassNotFoundException, SQLException {
        return customerDAO.delete(id);
    }


    public boolean update(CustomerDto dto) throws ClassNotFoundException, SQLException {
        Customer customer = new Customer(dto.getCustomerID(),dto.getCusName(),dto.getCusPhone(),dto.getCusEmail(),dto.getCusAddress(),dto.getCusUserID());
        return customerDAO.update(customer);
    }


    public CustomerDto searchById(String id) throws ClassNotFoundException, SQLException {

        Customer customer = customerDAO.searchById(id);
        CustomerDto customerDTO = new CustomerDto();
        customerDTO.setCustomerID(customer.getCustomerID());
        customerDTO.setCusName(customer.getCusName());
        customerDTO.setCusPhone(customer.getCusPhone());
        customerDTO.setCusEmail(customer.getCusEmail());
        customerDTO.setCusAddress(customer.getCusAddress());
        customerDTO.setCusUserID(customer.getCusUserID());

        return customerDTO;
    }


    public ArrayList<CustomerDto> searchByName(String name) throws ClassNotFoundException, SQLException {
        ArrayList<Customer> customers = customerDAO.searchByName(name);
        ArrayList<CustomerDto> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDto customerDTO = new CustomerDto();
            customerDTO.setCustomerID(customer.getCustomerID());
            customerDTO.setCusName(customer.getCusName());
            customerDTO.setCusPhone(customer.getCusPhone());
            customerDTO.setCusEmail(customer.getCusEmail());
            customerDTO.setCusAddress(customer.getCusAddress());
            customerDTO.setCusUserID(customer.getCusUserID());
            customerDTOs.add(customerDTO);
        }
        return customerDTOs;
    }

    public String suggestNextID() throws ClassNotFoundException, SQLException {
        return customerDAO.suggestNextID();
    }

}
