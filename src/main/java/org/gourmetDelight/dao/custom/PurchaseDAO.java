package org.gourmetDelight.dao.custom;

import org.gourmetDelight.dao.CrudDAO;

import org.gourmetDelight.entity.StockPurchase;


import java.sql.SQLException;

public interface PurchaseDAO extends CrudDAO<StockPurchase> {

    public String suggestNextID() throws ClassNotFoundException, SQLException;


}


