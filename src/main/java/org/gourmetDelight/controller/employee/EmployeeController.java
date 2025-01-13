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
import javafx.scene.layout.Pane;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.dto.employee.EmployeeDto;
import org.gourmetDelight.model.employee.EmployeeModel;
import org.gourmetDelight.util.DateAndTime;
import org.gourmetDelight.util.Navigations;
import org.gourmetDelight.util.ValidateUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TableColumn<EmployeeDto, String> empEmailCol;

    @FXML
    private TableColumn<EmployeeDto, String> empIdCol;

    @FXML
    private TableColumn<EmployeeDto, String> empNameCol;

    @FXML
    private TableColumn<EmployeeDto, String> empPhoneCol;

    @FXML
    private TableColumn<EmployeeDto, String> empPositionCol;

    @FXML
    private TableColumn<EmployeeDto, LocalDate> empHireDateCol;

    @FXML
    private AnchorPane employeeAnchorPane;

    @FXML
    private Pane employeeBodyPane;

    @FXML
    private TableView<EmployeeDto> employeeTable;


    @FXML
    private JFXButton clearTXT;

    @FXML
    private JFXButton empDeleteBtn;

    @FXML
    private JFXTextField empEmailTxt;

    @FXML
    private JFXTextField empHireDateTxt;

    @FXML
    private JFXButton empIdSearchBtn;

    @FXML
    private JFXTextField empIdTxt;

    @FXML
    private JFXButton empNameSearchBtn;

    @FXML
    private JFXTextField empNameTxt;

    @FXML
    private JFXTextField empPhoneTxt;


    @FXML
    private JFXButton empSaveBtn;

    @FXML
    private JFXButton empUpdateBtn;

    @FXML
    private JFXButton loadAllData;

    @FXML
    private JFXButton loadUsersBtn;

    @FXML
    private ChoiceBox<String> employeePositionnChoiceBox;

    private final EmployeeModel EMPLOYEE_MODEL;
    ValidateUtil validateUtil = new ValidateUtil();

    public EmployeeController() {
        this.EMPLOYEE_MODEL = new EmployeeModel();
        this.validateUtil = new ValidateUtil();
    }

    DateAndTime dateAndTime = new DateAndTime();
    @FXML
    void loadDate(MouseEvent event) {
        empHireDateTxt.setText(dateAndTime.addDate());
    }

    @FXML
    private JFXButton usersBtn;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        changeFocusText();

        empEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        empNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        empPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        empPositionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        empHireDateCol.setCellValueFactory(new PropertyValueFactory<>("hireDate"));


        try {
            loadEmployeeTable();
            setNextEmployeeID();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error loading employee data", e);
        }

    }

    public void changeFocusText(){
        TextField[] textFields = {empIdTxt,empNameTxt,empPhoneTxt,empEmailTxt,empHireDateTxt};

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
    void selectFromTable(MouseEvent event) {
        EmployeeDto selectedItem = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    @FXML
    void loadUsersPage(ActionEvent event) throws IOException {
        Navigations navigation = new Navigations();
        navigation.loadPage(employeeAnchorPane, "/view/employee/UsersPanel.fxml");
    }

    private LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }

    public ObservableList<EmployeeDto> getAllEmployees() throws ClassNotFoundException, SQLException {
        ArrayList<EmployeeDto> employeeList = EmployeeModel.getAll();
        return FXCollections.observableArrayList(employeeList);
    }


    public void loadEmployeeTable() throws ClassNotFoundException, SQLException {

        ObservableList<EmployeeDto> employeeDtos = getAllEmployees();
        employeeTable.setItems(employeeDtos);
    }

    @FXML
    void loadAllData(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadCustomerTable();
        clearFields();
        setNextEmployeeID();
        empSaveBtn.setDisable(false);
    }


    @FXML
    void clearTextfileds(ActionEvent event) {
        clearFields();
    }


    public void loadCustomerTable() throws ClassNotFoundException, SQLException {
        ObservableList<EmployeeDto> employeeDtos = getAllEmployees();
        employeeTable.setItems(employeeDtos);
        setNextEmployeeID();
    }


    public ObservableList<EmployeeDto> getAllCustomers() throws ClassNotFoundException, SQLException {
        ArrayList<EmployeeDto> customerList = EMPLOYEE_MODEL.getAll();
        return FXCollections.observableArrayList(customerList);
    }

    // fill the text fields after clicking the table row
    private void populateFields(EmployeeDto employee) {
        empIdTxt.setText(employee.getEmployeeID());
        empNameTxt.setText(employee.getName());
        //empPositionTxt.setText(employee.getPosition());
        employeePositionnChoiceBox.setValue(employee.getPosition());// setting the value to the choice box too
        empPhoneTxt.setText(employee.getPhone());
        empEmailTxt.setText(employee.getEmail());
        empHireDateTxt.setText(employee.getHireDate().toString());      //setText(LocalDate.now().toString());

        empSaveBtn.setDisable(true);
    }


    private void clearFields() {
        empIdTxt.clear();
        empNameTxt.clear();
        //empPositionTxt.clear();
        employeePositionnChoiceBox.setValue(null);
        empPhoneTxt.clear();
        empEmailTxt.clear();
        empHireDateTxt.clear();
    }


    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        if (header != null && !header.isEmpty()) {
            alert.setHeaderText(header);
        } else {
            alert.setHeaderText(null);
        }
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void saveEmployee(ActionEvent event) {

        try {

            if (validateInputs()) {
                LocalDate hireDate = parseDate(empHireDateTxt.getText().trim());

                EmployeeDto newEmployee = new EmployeeDto(
                        empIdTxt.getText().trim(),
                        empNameTxt.getText().trim(),
                        //empPositionTxt.getText().trim(),  // using the choice box instead of the textfield
                        employeePositionnChoiceBox.getValue().trim(),
                        empPhoneTxt.getText().trim(),
                        empEmailTxt.getText().trim(),
                        hireDate
                );


                String result = EMPLOYEE_MODEL.saveEmployee(newEmployee);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee Saved", result);

                // Refresh the TableView
                loadCustomerTable();

                // Clear input fields
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save customer.", e.getMessage());
        }
    }


    @FXML
    void updateEmployee(ActionEvent event) {
        EmployeeDto selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Employee Selected", "Please select a Employee to update.");
            return;
        }

        try {
            // Validate input fields
            if (validateInputs()) {
                LocalDate hireDate = parseDate(empHireDateTxt.getText().trim());
                // Create a employeeDto with updated data
                EmployeeDto updatedEmployee = new EmployeeDto(
                        empIdTxt.getText().trim(),
                        empNameTxt.getText().trim(),
                        //empPositionTxt.getText().trim(), used a choicebox
                        employeePositionnChoiceBox.getValue().trim(),
                        empPhoneTxt.getText().trim(),
                        empEmailTxt.getText().trim(),
                        hireDate
                );

                // Update employee in the database
                String result = EMPLOYEE_MODEL.updateEmployee(updatedEmployee);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee Updated", result);

                // Refresh the TableView
                loadEmployeeTable();

                // Clear input fields
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer.", e.getMessage());
        }
    }


    @FXML
    void deleteEmployee(ActionEvent event) {
        EmployeeDto selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Employee Selected", "Please select a Employee to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this Employee?");
        confirmationAlert.setContentText("Employee ID: " + selectedEmployee.getEmployeeID());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete customer from the database
                String deleteResult = EMPLOYEE_MODEL.deleteEmployee(selectedEmployee.getEmployeeID());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee Deleted", deleteResult);

                // Refresh the TableView
                loadCustomerTable();

                // Clear input fields
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete Employee.", e.getMessage());
            }
        }
    }


    @FXML
    void searchEmployeeId(ActionEvent event) {
        String searchId = empIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Employee ID", "Please enter a Employee ID to search.");
            return;
        }

        try {

            EmployeeDto foundEmployee = EMPLOYEE_MODEL.searchEmployeeById(searchId);

            if (foundEmployee != null) {
                populateFields(foundEmployee);
                showAlert(Alert.AlertType.INFORMATION, "Employee Found", "Search Results", "Employee ID: " + searchId + " has been found.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Employee Not Found", "No Employee found with ID: " + searchId);
                loadEmployeeTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search Employee.", e.getMessage());
        }
    }


    @FXML
    void searchEmployeeName(ActionEvent event) {
        String searchName = empNameTxt.getText().trim();
        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Name Provided", "Please enter a Name to search.");
            return;
        }

        try {

            ArrayList<EmployeeDto> foundEmployees = EMPLOYEE_MODEL.searchEmployeesByName(searchName);

            if (!foundEmployees.isEmpty()) {
                ObservableList<EmployeeDto> employeeList = FXCollections.observableArrayList(foundEmployees);
                employeeTable.setItems(employeeList);
                showAlert(Alert.AlertType.INFORMATION, "Employee Found", "Search Results", foundEmployees.size() + " Employee(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Employee Found", "No Employees found with the name: " + searchName);
                loadEmployeeTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search employees.", e.getMessage());
        } finally {
            clearFields();
        }
    }

    private void setNextEmployeeID() throws SQLException, ClassNotFoundException {
        empIdTxt.setText(EMPLOYEE_MODEL.suggestNextEmployeeID());
    }


    private boolean validateInputs() {
        String employeeID = empIdTxt.getText().trim();
        String empName = empNameTxt.getText().trim();
        String empPosition = employeePositionnChoiceBox.getValue().trim();
        String empPone = empPhoneTxt.getText().trim();
        String empEmail = empEmailTxt.getText().trim();
        String empHireDate = empHireDateTxt.getText().trim();

        if (employeeID.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Employee ID is required.", null);
            return false;
        }

        if (empName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Employee Name is required.", null);
            return false;
        }

        if (employeePositionnChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Employee Position is required.", null);
            return false;
        }

        if (empPone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Employee Phone Number is required.", null);
            return false;
        }

        if (empEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Employee Email is required.", null);
            return false;
        }

        if (empHireDate.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Employee HireDate is required.", null);
            return false;
        }

        return true;
    }


    @FXML
    void validateDate(KeyEvent event) {
        String date = empHireDateTxt.getText().trim();
        validateUtil.isValidDate(date, empHireDateTxt);
    }

    @FXML
    void validateEmail(KeyEvent event) {
        String empEmail = empEmailTxt.getText().trim();
        validateUtil.isValidEmail(empEmail, empEmailTxt);
    }

    @FXML
    void validateName(KeyEvent event) {
        String empName = empNameTxt.getText().trim();
        validateUtil.isValidName(empName, empNameTxt);
    }

    @FXML
    void validatePhone(KeyEvent event) {
        String empPhone = empPhoneTxt.getText().trim();
        validateUtil.isValidPhone(empPhone, empPhoneTxt);
    }

    @FXML
    void validatePosition(KeyEvent event) {
        String empPosition = employeePositionnChoiceBox.getValue().trim();
        validateUtil.isValidJobPosition(empPosition, employeePositionnChoiceBox);
    }



}
