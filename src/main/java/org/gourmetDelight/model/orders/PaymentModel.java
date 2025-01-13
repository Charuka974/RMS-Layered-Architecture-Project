package org.gourmetDelight.model.orders;

import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.orders.PaymentsDto;
import org.gourmetDelight.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {

    public String suggestNextPaymentID() throws ClassNotFoundException, SQLException {
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

    public PaymentsDto getPaymentById(String paymentId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String query = "SELECT * FROM Payments WHERE PaymentID = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, paymentId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new PaymentsDto(
                        rs.getString("PaymentID"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount"),
                        rs.getDate("PaymentDate").toLocalDate() // Adjust the date field according to your database
                );
            }
        }
        return null;
    }

    public boolean processPayments(ArrayList<PaymentsDto> paymentsDtos, Connection connection) throws SQLException, ClassNotFoundException {
        // Start a transaction
        connection.setAutoCommit(false); // Disable auto-commit for transaction

        for (PaymentsDto payment : paymentsDtos) {
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




}