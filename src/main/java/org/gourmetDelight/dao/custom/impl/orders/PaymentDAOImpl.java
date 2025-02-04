package org.gourmetDelight.dao.custom.impl.orders;

import org.gourmetDelight.dao.custom.PaymentDAO;
import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.entity.Payments;
import org.gourmetDelight.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {

    public boolean processPayments(ArrayList<Payments> paymentsDtos, Connection connection) throws SQLException, ClassNotFoundException {
        // Start a transaction
        connection.setAutoCommit(false); // Disable auto-commit for transaction

        for (Payments payment : paymentsDtos) {
            // SQL query to insert or update payments
            String insertPaymentSQL = "INSERT INTO Payments (PaymentID, PaymentMethod, Amount, PaymentDate) "
                    + "VALUES (?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE PaymentMethod = VALUES(PaymentMethod), Amount = VALUES(Amount), PaymentDate = VALUES(PaymentDate)";

            // Execute the insert or update query
            boolean paymentInserted = CrudUtil.execute(insertPaymentSQL,
                    payment.getPaymentID(),
                    payment.getPaymentMethod(),
                    payment.getAmount(),
                    payment.getPaymentDate());

            // If insertion failed, rollback the transaction and return false
            if (!paymentInserted) {
                connection.rollback();
                return false; // Rollback if payment processing fails
            }
        }

        // If all payments were processed successfully, commit the transaction
        connection.commit();
        return true; // Return true if payments are successfully inserted/updated
    }


    @Override
    public ArrayList<Payments> getAll() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public boolean save(Payments payment) throws ClassNotFoundException, SQLException {
        String insertPaymentSQL = "INSERT INTO Payments (PaymentID, PaymentMethod, Amount, PaymentDate) VALUES (?, ?, ?, ?)";
        boolean paymentInserted = CrudUtil.execute(insertPaymentSQL,
                payment.getPaymentID(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentDate());
        return paymentInserted;
    }

    @Override
    public boolean delete(String paymentID) throws ClassNotFoundException, SQLException {
        String deletePaymentSQL = "DELETE FROM Payments WHERE PaymentID = ?";
        boolean paymentDeleted = CrudUtil.execute(deletePaymentSQL, paymentID);
        return paymentDeleted;
    }

    @Override
    public boolean update(Payments dto) throws ClassNotFoundException, SQLException {
        return false;
    }

    @Override
    public Payments searchById(String id) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        String query = "SELECT * FROM Payments WHERE PaymentID = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Payments(
                        rs.getString("PaymentID"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount"),
                        rs.getDate("PaymentDate").toLocalDate() // Adjust the date field according to your database
                );
            }
        }
        return null;
    }

    @Override
    public ArrayList<Payments> searchByName(String name) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public String suggestNextID() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT PaymentID FROM Payments ORDER BY PaymentID DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }
}