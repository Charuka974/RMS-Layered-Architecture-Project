package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.SuperDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.custom.StockPurchaseDTOCustom;
import org.gourmetDelight.entity.custom.ReservationCustom;
import org.gourmetDelight.entity.custom.StockPurchaseCustom;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {

    public String selectEmail(String username) throws SQLException, ClassNotFoundException;

    public String selectPhone(String username) throws SQLException, ClassNotFoundException;
    public String getRole(String username, String password) throws ClassNotFoundException, SQLException;

    public ReservationCustom searchReservationsByReserveID(String customerID) throws ClassNotFoundException, SQLException;

    public ReservationCustom searchById(String reservationID) throws ClassNotFoundException, SQLException;
    public LocalDateTime findDateTime(String reservationID) throws SQLException, ClassNotFoundException;

    public ArrayList<ReservationCustom> searchReservationsByCustomerID(String customerID) throws ClassNotFoundException, SQLException;
    public ArrayList<ReservationCustom> getAllReservationDetails() throws ClassNotFoundException, SQLException;

    public StockPurchaseDTOCustom searchStochPurchaseByID(String purchaseID) throws SQLException, ClassNotFoundException;


    public ArrayList<StockPurchaseCustom> getAllStockPurchases() throws ClassNotFoundException, SQLException;

}
