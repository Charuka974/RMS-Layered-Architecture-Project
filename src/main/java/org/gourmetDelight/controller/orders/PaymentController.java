package org.gourmetDelight.controller.orders;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.gourmetDelight.db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PaymentController {

    @FXML
    private AnchorPane paymentAnchorPane;

    @FXML
    private Label paymentIdLabel;

    @FXML
    private Label paymentMethodChoiceLabel;

    @FXML
    private JFXButton placeOrderBtn;

    @FXML
    private Label totalAmountLabel;

    static String orderID;
    static boolean completed = false;

    @FXML
    void placeOrder(ActionEvent event) throws JRException, SQLException, ClassNotFoundException {
        javafx.stage.Stage stage = (javafx.stage.Stage) paymentAnchorPane.getScene().getWindow();
        // Close the window
        stage.close();
        if(completed){
            printBill();
        }
    }

    public void setPaymentId(String paymentId) {
        if (paymentIdLabel != null) {
            paymentIdLabel.setText(paymentId);
        } else {
            totalAmountLabel.setText("No Payment ID Selected");
        }
    }

    public void setAmount(String totalAmount) {
        if (totalAmountLabel != null) {
            totalAmountLabel.setText(totalAmount);
        } else {
            totalAmountLabel.setText("No Amount Selected");
        }
    }


    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethodChoiceLabel != null) {
            paymentMethodChoiceLabel.setText(paymentMethod);
        } else {
            paymentMethodChoiceLabel.setText("No Amount Selected");
        }

    }

    public void setOrderId(String orderId) {
        this.orderID = orderId;
    }

    public void printBill() throws JRException, SQLException, ClassNotFoundException {
        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/RMS_Bill.jrxml"));
        Connection connection = DBConnection.getInstance().getConnection();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P_OrderID", orderID); // Use the correct parameter key here
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer.viewReport(jasperPrint, false);
    }

    public void setIfCompleted(boolean completedOrNot) {
        this.completed = completedOrNot;
    }


}
