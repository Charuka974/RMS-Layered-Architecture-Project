package org.gourmetDelight.controller.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.gourmetDelight.bo.BOFactory;
import org.gourmetDelight.bo.custom.EmployeeBO;
import org.gourmetDelight.bo.custom.UserBO;


import org.gourmetDelight.util.EmailUtil;
import org.gourmetDelight.util.SmsSend;
import org.gourmetDelight.util.ValidateUtil;


import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;



public class ForgotPasswordController {

    SmsSend smsSend = new SmsSend();


    @FXML
    private Pane setNewPasswordPane;

    @FXML
    private JFXTextField newPasswordConfirmTxt;

    @FXML
    private JFXTextField newPasswordTxt;

    @FXML
    private JFXButton confirmNewPasswordBtn;

    @FXML
    private JFXButton confirmUserNameBtn;

    @FXML
    private JFXButton confirmOtpBtn;

    @FXML
    private AnchorPane forgotPasswordAnchorPane;

    @FXML
    private JFXButton logInPanelButton;

    @FXML
    private JFXTextField otpTxt;

    @FXML
    private Label recieverEmailLbl;

    @FXML
    private Label recieverPhoneNumberLbl;

    @FXML
    private JFXButton sendOtpSMSBtn;

    @FXML
    private JFXButton sendOtpEmailBtn;

    @FXML
    private JFXTextField txtUserName;

    public void initialize() {
        setNewPasswordPane.setVisible(false);
    }

    private int intOtp;
    private String username;
    private String phoneNumber;
    private String recieverEmail;

    ValidateUtil validateUtil = new ValidateUtil();

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USERS);
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.EMPLOYEE);

    public ForgotPasswordController() {

    }

    @FXML
    void confirmUsername(ActionEvent event) throws SQLException, ClassNotFoundException {
        username = txtUserName.getText();
        boolean userExists = userBO.validateUserForgetPassword(username);

        if (userExists) {
            recieverEmail = employeeBO.selectEmail(username);
            phoneNumber = employeeBO.selectPhone(username);

            recieverEmailLbl.setText(recieverEmail);
            recieverPhoneNumberLbl.setText(phoneNumber);

        }else {
        JOptionPane.showMessageDialog(null, "Invalid Username");
        }


    }

    @FXML
    void validatePassword(KeyEvent event) {

        validateUtil.isValidPassword(newPasswordTxt.getText(), newPasswordTxt);
    }

    @FXML
    void validatePassword2(KeyEvent event) {

        validateUtil.isValidPassword(newPasswordConfirmTxt.getText(), newPasswordConfirmTxt);
    }

    @FXML
    void confirmOtp(ActionEvent event) throws IOException {
        confirmOtp();
    }

    @FXML
    void getLogInUpPanel(ActionEvent event) {
        forgotPasswordAnchorPane.setVisible(false);
    }


    @FXML
    void sendOtpEmail(ActionEvent event) {
        System.out.println(recieverEmail);
        int emailOTP = createOtp();
        String otp = Integer.toString(emailOTP);

        System.out.println(emailOTP);

        EmailUtil.sendEmail(recieverEmail,"Your OTP For Gourmet Delight", otp);

    }

    @FXML
    void sendOtpSMS(ActionEvent event)throws IOException {
        System.out.println(phoneNumber);
        int smsOTP = createOtp();
        System.out.println(smsOTP);
        //sendsms(phoneNumber, smsOTP);


    }

    private int createOtp() {
        if (username != null) {

            Random rand = new Random();
            intOtp = 100000 + rand.nextInt(900000);

        } else {
            JOptionPane.showMessageDialog(null, "Enter a Valid Username");
        }

        return intOtp;
    }


    void confirmOtp() throws IOException {
        final int textFieldOtp = Integer.parseInt(otpTxt.getText());

        if(intOtp == textFieldOtp){
            setNewPasswordPane.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid OTP");
        }

    }

    @FXML
    void setNewPassword(ActionEvent event) throws SQLException, ClassNotFoundException {
        String newPassword = newPasswordTxt.getText();
        String confirmPassword = newPasswordConfirmTxt.getText();
        if (newPassword.equals(confirmPassword)) {
            userBO.setNewPassword(username, newPassword);
        }
        else {
            JOptionPane.showMessageDialog(null, "Your confirm password is Incorrect");
        }
    }


}



