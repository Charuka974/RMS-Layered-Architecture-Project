package org.gourmetDelight.controller.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.gourmetDelight.dto.employee.EmployeeDto;
import org.gourmetDelight.dto.employee.UserDto;
import org.gourmetDelight.model.employee.UsersModel;
import org.gourmetDelight.util.DateAndTime;
import org.gourmetDelight.util.KeepUser;
import org.gourmetDelight.util.ValidateUtil;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.gourmetDelight.model.employee.UsersModel.getAllUsers;
import static org.gourmetDelight.util.Navigations.*;

public class UsersController implements Initializable {

    @FXML
    private JFXButton backToEmployeesBtn, clearTXT, loadAllData, userDeleteBtn, userIdSearchBtn,
            usernameSearchBtn, userSaveBtn, userUpdateBtn;

    @FXML
    private TableColumn<UserDto, String> userEmpIdCol, userIdCol, userPasswordCol, usernameCol;

    @FXML
    private TableColumn<UserDto, LocalDate> userLoginTimeCol;

    @FXML
    private JFXTextField userIdTxt, usernameTxt, userPasswordTxt, userLoginTimeTxt, userEmpIdTxt;

    @FXML
    private AnchorPane usersAnchorpane;

    @FXML
    private JFXTextField userEmpNameTxt;

    @FXML
    private TableView<UserDto> usersTable;

    private final UsersModel USERS_MODEL;
    private final ValidateUtil validateUtil;
    private final DateAndTime dateAndTime;

    public UsersController() {
        this.USERS_MODEL = new UsersModel();
        this.validateUtil = new ValidateUtil();
        this.dateAndTime = new DateAndTime();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeFocusText();
        try {
            initializeTableColumns();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            loadUsersTable();
            setNextUserId();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                    "Failed to load user data.", e.getMessage());
        }


    }

    public void changeFocusText(){
        TextField[] textFields = {userIdTxt,usernameTxt,userPasswordTxt,userLoginTimeTxt,userEmpNameTxt,userEmpIdTxt};

        for (int i = 0; i < textFields.length; i++) {
            int currentIndex = i; // Capture the current index for the lambda
            textFields[i].setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER -> {
                        // Calculate the next index (loop back to the first one if at the end)
                        int nextIndex = (currentIndex + 1) % textFields.length;
                        textFields[nextIndex].requestFocus(); // Move focus to the next TextField
                    }
                }
            });
        }}

    @FXML
    void selectFromTable(MouseEvent event) throws SQLException, ClassNotFoundException {
        UserDto selectedItem = usersTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
            userEmpNameTxt.setText(USERS_MODEL.getEmployeeName(selectedItem.getEmployeeID()));
        }
    }

    private void initializeTableColumns() throws SQLException, ClassNotFoundException {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        showOnlyCurrentUserPasswords();
        //userPasswordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        userLoginTimeCol.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        userEmpIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
    }

    public void showOnlyCurrentUserPasswords() throws SQLException, ClassNotFoundException {
        String currentUserID = KeepUser.getInstance().getUserID(); // Get the current user's ID

        // Set up the cell value factory for the password column
        userPasswordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Set up the cell factory to control how passwords are displayed
        userPasswordCol.setCellFactory(column -> new TableCell<UserDto, String>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);

                if (empty || password == null || password.isEmpty()) {
                    setText(null);
                } else {
                    // Get the user associated with this cell
                    UserDto currentUser = getTableView().getItems().get(getIndex());

                    // Compare the user ID with the current user's ID
                    if (currentUser.getUserId().equals(currentUserID)) {
                        // Show the actual password for the current user
                        setText(password);
                    } else {
                        // Mask the password for other users
                        setText("*".repeat(password.length()));
                    }
                }
            }
        });



    }



    @FXML
    void backToEmployees(ActionEvent event) {
        setInvisible(usersAnchorpane);
    }

    @FXML
    void clearTextFields(ActionEvent event) {
        clearFields();
        userSaveBtn.setDisable(false);
    }

    @FXML
    void deleteUser(ActionEvent event) {
        UserDto selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a user to delete.", null);
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert("Confirm Deletion",
                "Are you sure you want to delete this user?",
                "User ID: " + selectedUser.getUserId());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String deleteResult = USERS_MODEL.deleteUser(selectedUser.getUserId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "User Deleted", deleteResult);
                loadUsersTable();
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.", e.getMessage());
            }
        }

    }

    @FXML
    void loadAllData(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadUsersTable();
        clearFields();
        setNextUserId();
        userSaveBtn.setDisable(false);
    }

    @FXML
    void saveUser(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            UserDto newUser = createUserFromFields();
            String result = USERS_MODEL.saveUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User Saved", result);
            loadUsersTable();
            clearFields();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user.", e.getMessage());
        }
    }

    @FXML
    void updateUser(ActionEvent event) {
        UserDto selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a user to update.", null);
            return;
        }

        if (!validateInputs()) return;

        try {
            UserDto updatedUser = createUserFromFields();
            String result = USERS_MODEL.updateUser(updatedUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User Updated", result);
            loadUsersTable();
            clearFields();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user.", e.getMessage());
        }
    }

    @FXML
    void searchUserId(ActionEvent event) {
        String searchId = userIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required",
                    "Please enter a User ID to search.", null);
            return;
        }

        try {
            UserDto foundUser = USERS_MODEL.searchUserById(searchId);
            if (foundUser != null) {
                populateFields(foundUser);
                showAlert(Alert.AlertType.INFORMATION, "User Found", "Search Results",
                        "User ID: " + searchId + " has been found.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No User found with ID: " + searchId, null);
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search user.", e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (userIdTxt.getText().trim().isEmpty() ||
                usernameTxt.getText().trim().isEmpty() ||
                userPasswordTxt.getText().trim().isEmpty() ||
                userEmpIdTxt.getText().trim().isEmpty() ||
                userLoginTimeTxt.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "All fields are required.", null);
            return false;
        }

        try {
            LocalDate.parse(userLoginTimeTxt.getText().trim());  // Validate date format
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date",
                    "Please enter a valid date (YYYY-MM-DD).", null);
            return false;
        }
        return true;
    }

    private UserDto createUserFromFields() {
        return new UserDto(
                userIdTxt.getText().trim(),
                usernameTxt.getText().trim(),
                userPasswordTxt.getText().trim(),
                LocalDate.parse(userLoginTimeTxt.getText().trim()),
                userEmpIdTxt.getText().trim()
        );
    }

    private void loadUsersTable() throws ClassNotFoundException, SQLException {
        ObservableList<UserDto> userDtos = FXCollections.observableArrayList(getAllUsers());
        usersTable.setItems(userDtos);
        setNextUserId();
    }

    private void setNextUserId() throws SQLException, ClassNotFoundException {
        userIdTxt.setText(USERS_MODEL.suggestNextUserId());
    }

    private void populateFields(UserDto user) throws SQLException{
//        userIdTxt.setText(user.getUserId());
//        usernameTxt.setText(user.getUsername());
//        userPasswordTxt.setText(user.getPassword());
//        userLoginTimeTxt.setText(user.getLoginTime().toString());
//        userEmpIdTxt.setText(user.getEmployeeID());


        String currentUserID = KeepUser.getInstance().getUserID(); // Get the current user's ID

        // Populate the text fields with user details
        userIdTxt.setText(user.getUserId());
        usernameTxt.setText(user.getUsername());

        // Conditionally display the password based on the current user
        if (user.getUserId().equals(currentUserID)) {
            userUpdateBtn.setDisable(false);
            userPasswordTxt.setText(user.getPassword()); // Show actual password for the current user
        } else {
            userUpdateBtn.setDisable(true);
            userPasswordTxt.setText("*".repeat(user.getPassword().length())); // Mask the password for other users
        }

        userLoginTimeTxt.setText(user.getLoginTime().toString());
        userEmpIdTxt.setText(user.getEmployeeID());

        userSaveBtn.setDisable(true);

    }


    private void clearFields() {
        userIdTxt.clear();
        usernameTxt.clear();
        userPasswordTxt.clear();
        userLoginTimeTxt.clear();
        userEmpIdTxt.clear();
        userEmpNameTxt.clear();

    }

    private Optional<ButtonType> showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void searchUsername(ActionEvent actionEvent) {
        String searchUsername = usernameTxt.getText().trim();
        if (searchUsername.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Username Provided", "Please enter a Username to search.");
            return;
        }

        try {

            ArrayList<UserDto> founduser = USERS_MODEL.searchUsersByUsername(searchUsername);

            if (!founduser.isEmpty()) {
                ObservableList<UserDto> userList = FXCollections.observableArrayList(founduser);
                usersTable.setItems(userList);
                showAlert(Alert.AlertType.INFORMATION, "User Found", "Search Results", founduser.size() + " User found with the username: " + searchUsername);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No User Found", "No User found with the username: " + searchUsername);
                loadUsersTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search user.", e.getMessage());
        } finally {
            clearFields();
        }

    }

    @FXML
    public void validateUserName(KeyEvent keyEvent) {
        String username = usernameTxt.getText().trim();
        validateUtil.isValidUsername(username, usernameTxt);
    }

    @FXML
    public void validatePassword(KeyEvent keyEvent) {
        String password = userPasswordTxt.getText().trim();
        validateUtil.isValidPassword(password, userPasswordTxt);
    }

    @FXML
    public void validateTime(KeyEvent keyEvent) {
    }

    @FXML
    public void getEmployeeId(ActionEvent actionEvent) {
        try {
            String employeeId = USERS_MODEL.searchEmployeeName(userEmpNameTxt.getText().trim());
            if (employeeId == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Employee Not Found", "The employee you are searching for could not be found.");
                userEmpIdTxt.setText(null);
            }else{
                userEmpIdTxt.setText(employeeId);
            }

        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to get employee ID.", e.getMessage());
        }
    }


}
