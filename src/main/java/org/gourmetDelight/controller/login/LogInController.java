package org.gourmetDelight.controller.login;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.gourmetDelight.model.login.LogInModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import javafx.fxml.FXML;
import org.gourmetDelight.util.KeepUser;

import static org.gourmetDelight.util.Navigations.loadPanel;


public class LogInController {

    @FXML
    public AnchorPane anchorPane;

    @FXML
    private JFXButton forgotPasswordBtn;

    @FXML
    private JFXButton logInBtn;

    @FXML
    private Pane logInPane;

    @FXML
    private JFXTextField visiblePasswordTxt;

    @FXML
    private ImageView logo;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private ImageView setPasswordVisibleImage;

    @FXML
    private JFXTextField txtUserName;


    @FXML
    private JFXButton signUpPanelButton;

    @FXML
    private Pane loginBodyPane;


    @FXML
    void logIn(ActionEvent event) throws IOException {
        final String inputUserName = txtUserName.getText();
        final String inputPassword = txtPassword.getText();

        logInValidation(inputUserName, inputPassword);
        //loadPanel(anchorPane, "/view/home/ManagerHomePage.fxml");  //it is easy to log in for now , remove this
    }

    @FXML
    void getSignUpPanel(ActionEvent event) throws IOException {
        loadSignUpPanel();
    }
    @FXML
    public void initialize() {
        changeFocusText();
//        loginBodyPane.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ENTER) {
//                logInBtn.fire();
//            }
//        });



    }

    public void changeFocusText() {
        TextField[] textFields = {txtUserName, txtPassword};

        for (int i = 0; i < textFields.length; i++) {
            int currentIndex = i; // Capture the current index for the lambda
            textFields[i].setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (currentIndex == textFields.length - 1) {
                        // If it's the last TextField, trigger the login button
                        logInBtn.fire();
                    } else {
                        // Otherwise, move to the next TextField
                        int nextIndex = (currentIndex + 1) % textFields.length;
                        textFields[nextIndex].requestFocus();
                    }
                }
            });
        }

        // Add a global KeyPressed handler for the parent pane
        loginBodyPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !txtPassword.isFocused() && !txtUserName.isFocused()) {
                logInBtn.fire(); // Ensure the login button fires if neither TextField is focused
            }
        });
    }
    

    @FXML
    void setPasswordInvisible(MouseEvent event) {
        visiblePasswordTxt.setVisible(false);
        txtPassword.setVisible(true);
        txtPassword.setText(visiblePasswordTxt.getText());
        setPasswordVisibleImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/icons/closedEye.jpg")).toExternalForm()));


    }

    @FXML
    void setPasswordVisible(MouseEvent event) {
        visiblePasswordTxt.setVisible(true);
        txtPassword.setVisible(false);
        visiblePasswordTxt.setText(txtPassword.getText());
        setPasswordVisibleImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/icons/openedEye.jpg")).toExternalForm()));


    }


    @FXML
    void forgotPasswordPanel(ActionEvent event) throws IOException {
        loginBodyPane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/login/ForgotPasswordPanel.fxml")));
    }


    private void loadSignUpPanel() throws IOException {
        loginBodyPane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/login/SignUpPanel.fxml")));
    }

    private final LogInModel LOGIN_MODEL;

    public LogInController() {
        this.LOGIN_MODEL = new LogInModel();
    }

    public void logInValidation(String inputUserName, String inputPassword) {


        try {
            boolean isValid = validateUser(inputUserName, inputPassword);
            if (isValid) {
                getThePosition(inputUserName, inputPassword);
            } else {
                invalidErrorMessage();
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getThePosition(String inputUserName, String inputPassword) {
        try {
            String role = selectPosition(inputUserName, inputPassword);

            if (role != null) {
                switch (role) {
                    case "Admin" -> {
                        loadPanel(anchorPane, "/view/home/ManagerHomePage.fxml");
                        getCurrentUserID(inputUserName, inputPassword);
                    }
                    case "Manager" -> {
                        loadPanel(anchorPane, "/view/home/ManagerHomePage.fxml");
                        getCurrentUserID(inputUserName, inputPassword);
                    }
                    case "Cashier" -> {
                        loadPanel(anchorPane, "/view/home/CashierHomePage.fxml");
                        getCurrentUserID(inputUserName, inputPassword);

                    }
                    default -> JOptionPane.showMessageDialog(null, "Invalid Job Role", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                invalidErrorMessage();
            }
        } catch (ClassNotFoundException | SQLException e) {
            invalidErrorMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

// saving userID of the user thats loging in
    public void getCurrentUserID(String inputUserName, String inputPassword) {
        try {
            String userID = getUserID(inputUserName, inputPassword);
            System.out.println("Found userID: " + userID);

            if (userID != null) {
                KeepUser.getInstance().setUserID(userID);
                System.out.println("Saved userID in KeepUser: " + KeepUser.getInstance().getUserID());  // Confirm it is saved
            } else {
                invalidErrorMessage();
                System.out.println("Setting userID failed");
            }
        } catch (ClassNotFoundException | SQLException e) {
            invalidErrorMessage();
            e.printStackTrace();
        }
    }




    private void invalidErrorMessage() {
        JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String selectPosition(String username, String password) throws ClassNotFoundException, SQLException {
        return LOGIN_MODEL.getRole(username, password);
    }

    public boolean validateUser(String username, String password) throws ClassNotFoundException, SQLException {
        return LOGIN_MODEL.validateUser(username, password);
    }

    public String getUserID(String username, String password) throws ClassNotFoundException, SQLException {
        return LOGIN_MODEL.getUserID(username, password);
    }



}
