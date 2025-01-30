package org.gourmetDelight.controller.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import org.gourmetDelight.entity.Employee;
import org.gourmetDelight.entity.User;
import org.gourmetDelight.dao.custom.impl.employee.EmployeeDAOImpl;
import org.gourmetDelight.dao.custom.impl.employee.UsersDAOImpl;
import org.gourmetDelight.util.DateAndTime;
import org.gourmetDelight.util.ValidateUtil;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SignUpController extends Component implements Initializable {

    @FXML
    private JFXButton logInPanelButton;

    @FXML
    private JFXTextField signDateTxt;

    @FXML
    private JFXTextField signEmpEmailTxt;

    @FXML
    private JFXTextField signEmpIdTxt;

    @FXML
    private JFXTextField signEmpNameTxt;

    @FXML
    private JFXTextField signEmpPhoneTxt;

    @FXML
    private JFXTextField signEmpPositionTxt;

    @FXML
    private JFXTextField signPasswordTxt;

    @FXML
    private AnchorPane signUpAnchorPane;

    @FXML
    private JFXButton signUpBtn;

    @FXML
    private JFXTextField signUserIdTxt;

    @FXML
    private VBox detailPane;

    @FXML
    private JFXTextField passkeyTxt;

    @FXML
    private JFXTextField signPasswordTxt1;

    @FXML
    private JFXButton confirmBtn;

    @FXML
    private JFXTextField signUsernameTxt;

    private EmployeeDAOImpl employeeDAOImpl = new EmployeeDAOImpl();
    private UsersDAOImpl usersDAOImpl = new UsersDAOImpl();
    DateAndTime dateAndTime = new DateAndTime();
    ValidateUtil validateUtil = new ValidateUtil();

    public void initialize(URL url, ResourceBundle rb) {
        signDateTxt.setText(dateAndTime.addDate());
        try {
            setEmployeeId();
            setUserId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        detailPane.setVisible(false);
        signUpBtn.setVisible(false);
        changeFocusText();
    }

    public void changeFocusText() {
        // Add all relevant TextFields to the array
        TextField[] textFields = {
                signEmpIdTxt, signEmpNameTxt, signEmpPositionTxt,signEmpPhoneTxt,
                signEmpEmailTxt, signDateTxt, signUserIdTxt, signUsernameTxt, signPasswordTxt, signPasswordTxt1
        };

        // Loop through each TextField to set the key press event
        for (int i = 0; i < textFields.length; i++) {
            int currentIndex = i; // Capture the current index for the lambda
            textFields[i].setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DOWN) {
                        // Otherwise, move to the next TextField
                        int nextIndex = (currentIndex + 1) % textFields.length;
                        textFields[nextIndex].requestFocus();

                }
            });
        }

    }

    @FXML
    void getLogInUpPanel(ActionEvent event) {
        signUpAnchorPane.setVisible(false);
    }


    public void setEmployeeId() throws SQLException, ClassNotFoundException {
        signEmpIdTxt.setText(employeeDAOImpl.suggestNextID());
    }

    public void setUserId() throws SQLException, ClassNotFoundException {
       signUserIdTxt.setText(usersDAOImpl.suggestNextID());
    }


    @FXML
    void signUp(ActionEvent event) {
        if(validateFields()) {
            if (saveEmployeeData()) {
                if (saveUserData()) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Signed Up Successfully", "You can now Log in to the System");

                }
                showAlert(Alert.AlertType.ERROR, "Error", "SignUp Failed", "Failed to sign up");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "SignUp Failed", "Failed to sign up");
            }
        }

    }

    private LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }


    private boolean saveEmployeeData() {

        boolean success = false;
        try {

                LocalDate hireDate = parseDate(signDateTxt.getText().trim());

                Employee newEmployee = new Employee(
                        signEmpIdTxt.getText().trim(),
                        signEmpNameTxt.getText().trim(),
                        signEmpPositionTxt.getText().trim(),
                        signEmpPhoneTxt.getText().trim(),
                        signEmpEmailTxt.getText().trim(),
                        hireDate
                );


                boolean result = employeeDAOImpl.save(newEmployee);

            success = true;

        } catch (ClassNotFoundException | SQLException e) {

        }
        return success;
    }


    private boolean saveUserData(){
        boolean success = false;
        try {
            User newUser = createUserFromFields();
            boolean result = usersDAOImpl.save(newUser);
            success = true;

        } catch (ClassNotFoundException | SQLException e) {

        }
        return success;
    }


    private boolean validateFields() {
        // Regex patterns
        String phonePattern = "^(07\\d{8}|0\\d{2}\\d{7})$";
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$"; // Simple email validation
        String namePattern = "^[A-Za-z ]+$"; // Allows only letters and spaces

        // Validate phone number
        if (!Pattern.matches(phonePattern, signEmpPhoneTxt.getText().trim())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Phone Number", "Please enter a valid Sri Lankan phone number.");
            return false;
        }

        // Validate email
        if (!Pattern.matches(emailPattern, signEmpEmailTxt.getText().trim())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        // Validate name
        if (!Pattern.matches(namePattern, signEmpNameTxt.getText().trim())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Name", "Please enter a valid name (letters and spaces only).");
            return false;
        }

        // Validate position (cannot be empty)
        if (signEmpPositionTxt.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Position", "Position cannot be empty.");
            return false;
        }

        // Validate passwords
        if (!signPasswordTxt.getText().equals(signPasswordTxt1.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password Mismatch", "Passwords do not match. Please check again.");
            return false;
        }

        return true; // All validations passed
    }



    @FXML
    void validatePassword(KeyEvent event) {
        validateUtil.isValidPassword(signPasswordTxt.getText(), signPasswordTxt);
    }

    @FXML
    void validatePassword1(KeyEvent event) {
        validateUtil.isValidPassword(signPasswordTxt1.getText(), signPasswordTxt1);
    }

    @FXML
    void validateUsername(KeyEvent event) {
        validateUtil.isValidUsername(signUsernameTxt.getText(), signUsernameTxt);
    }

    private User createUserFromFields() {
        String password = null;
        if(signPasswordTxt.getText().equals(signPasswordTxt1.getText())){
            password = signPasswordTxt.getText();
        }else{
            new Alert(Alert.AlertType.ERROR, "Check Confirm Password").show();
        }
        return new User(
                signUserIdTxt.getText().trim(),
                signUsernameTxt.getText().trim(),
                password,
                LocalDate.parse(signDateTxt.getText().trim()),
                signEmpIdTxt.getText().trim()
        );
    }


    @FXML
    void passkeyConfirmBtn(ActionEvent event) {
        if(passkeyTxt.getText().equals("Admin")){
            detailPane.setVisible(true);
            signUpBtn.setVisible(true);
        }else{
            new Alert(Alert.AlertType.ERROR, "Wrong PassKey !!!").show();
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}


