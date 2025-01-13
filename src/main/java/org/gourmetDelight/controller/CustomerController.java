package org.gourmetDelight.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.model.CustomerModel;
import org.gourmetDelight.util.KeepUser;
import org.gourmetDelight.util.ValidateUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private TableColumn<CustomerDto, String> cusAddressColumn;

    @FXML
    private JFXTextField cusAddressTxt;

    @FXML
    private JFXButton cusDeleteBtn;

    @FXML
    private TableColumn<CustomerDto, String> cusEmailColumn;

    @FXML
    private JFXTextField cusEmailTxt;

    @FXML
    private TableColumn<CustomerDto, String> cusIdColumn;

    @FXML
    private JFXButton cusIdSearchBtn;

    @FXML
    private JFXTextField cusIdTxt;

    @FXML
    private TableColumn<CustomerDto, String> cusNameColumn;

    @FXML
    private JFXButton cusNameSearchBtn;

    @FXML
    private JFXTextField cusNameTxt;

    @FXML
    private TableColumn<CustomerDto, String> cusPhoneColumn;

    @FXML
    private JFXTextField cusPhoneTxt;

    @FXML
    private JFXButton cusSaveBtn;

    @FXML
    private JFXButton clearTXT;

    @FXML
    private JFXButton cusUpdateBtn;

    @FXML
    private TableColumn<CustomerDto, String> cusUserIdColumn;

    @FXML
    private Label cusUserIdLabel;

    @FXML
    private AnchorPane customerAnchorPane;

    @FXML
    private JFXButton loadAllData;

    @FXML
    private Pane customerBodyPane;

    @FXML
    private TableView<CustomerDto> customerTable;

    private final CustomerModel CUSTOMER_MODEL;
    ValidateUtil validateUtil = new ValidateUtil();

    public CustomerController() {
        this.CUSTOMER_MODEL = new CustomerModel();
        this.validateUtil = new ValidateUtil();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        loadUserID();

        // Initialize the TableView columns
        cusIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cusNameColumn.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        cusPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("cusPhone"));
        cusEmailColumn.setCellValueFactory(new PropertyValueFactory<>("cusEmail"));
        cusAddressColumn.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        cusUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("cusUserID"));

        // Load customer data into the TableView
        try {
            loadCustomerTable();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load customer data.", e.getMessage());
        }

        cusUpdateBtn.setDisable(true);
        cusDeleteBtn.setDisable(true);
        changeFocusText();
    }

    public void changeFocusText() {
        // Add all relevant TextFields to the array
        TextField[] textFields = {
                cusIdTxt, cusNameTxt, cusPhoneTxt, cusEmailTxt, cusAddressTxt
        };

        // Loop through each TextField to set the key press event
        for (int i = 0; i < textFields.length; i++) {
            int currentIndex = i; // Capture the current index for the lambda
            textFields[i].setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    // Otherwise, move to the next TextField
                    int nextIndex = (currentIndex + 1) % textFields.length;
                    textFields[nextIndex].requestFocus();

                }
            });
        }

    }

    @FXML
    void selectFromTable(MouseEvent event) {
        CustomerDto selectedItem = customerTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    public void loadUserID(){
        String currentUserID = KeepUser.getInstance().getUserID();
        cusUserIdLabel.setText(currentUserID);
    }

    @FXML
    void loadAllData(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadCustomerTable();
        clearFields();
        setNextCustomerID();
        cusSaveBtn.setDisable(false);
        cusUpdateBtn.setDisable(true);
        cusDeleteBtn.setDisable(true);
        loadUserID();
    }


    @FXML
    void clearTextfileds(ActionEvent event) {
        clearFields();
    }


    public void loadCustomerTable() throws ClassNotFoundException, SQLException {
        ObservableList<CustomerDto> customerDtos = getAllCustomers();
        customerTable.setItems(customerDtos);
        setNextCustomerID();
    }


    public ObservableList<CustomerDto> getAllCustomers() throws ClassNotFoundException, SQLException {
        ArrayList<CustomerDto> customerList = CUSTOMER_MODEL.getAll();
        return FXCollections.observableArrayList(customerList);
    }

    // fill the text fields after clicking the table row
    private void populateFields(CustomerDto customer) {
        cusIdTxt.setText(customer.getCustomerID());
        cusNameTxt.setText(customer.getCusName());
        cusPhoneTxt.setText(customer.getCusPhone());
        cusEmailTxt.setText(customer.getCusEmail());
        cusAddressTxt.setText(customer.getCusAddress());
        cusUserIdLabel.setText(customer.getCusUserID());

        cusSaveBtn.setDisable(true);
        cusUpdateBtn.setDisable(false);
        cusDeleteBtn.setDisable(false);
    }


    private void clearFields() {
        cusIdTxt.clear();
        cusNameTxt.clear();
        cusPhoneTxt.clear();
        cusEmailTxt.clear();
        cusAddressTxt.clear();
        //cusUserIdLabel.setText(null);   we do not need to make the userID label empty because it does not change

        cusUpdateBtn.setDisable(true);
        cusDeleteBtn.setDisable(true);
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
    void saveCustomer(ActionEvent event) {

        try {
            // Validate input fields
            if (validateInputs()) {
                // Create a new CustomerDto from TextFields
                CustomerDto newCustomer = new CustomerDto(
                        cusIdTxt.getText().trim(),
                        cusNameTxt.getText().trim(),
                        cusPhoneTxt.getText().trim(),
                        cusEmailTxt.getText().trim(),
                        cusAddressTxt.getText().trim(),
                        cusUserIdLabel.getText().trim()
                );

                // Save customer to the database
                String result = CUSTOMER_MODEL.saveCustomer(newCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Saved", result);

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
    void updateCustomer(ActionEvent event) {
        CustomerDto selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Customer Selected", "Please select a customer to update.");
            return;
        }

        try {
            // Validate input fields
            if (validateInputs()) {
                // Create a CustomerDto with updated data
                CustomerDto updatedCustomer = new CustomerDto(
                        cusIdTxt.getText().trim(),
                        cusNameTxt.getText().trim(),
                        cusPhoneTxt.getText().trim(),
                        cusEmailTxt.getText().trim(),
                        cusAddressTxt.getText().trim(),
                        cusUserIdLabel.getText().trim()
                );

                // Update customer in the database
                String result = CUSTOMER_MODEL.updateCustomer(updatedCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Updated", result);

                // Refresh the TableView
                loadCustomerTable();

                // Clear input fields
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer.", e.getMessage());
        }
    }


    @FXML
    void deleteCustomer(ActionEvent event) {
        CustomerDto selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Customer Selected", "Please select a customer to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this customer?");
        confirmationAlert.setContentText("Customer ID: " + selectedCustomer.getCustomerID());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete customer from the database
                String deleteResult = CUSTOMER_MODEL.deleteCustomer(selectedCustomer.getCustomerID());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Deleted", deleteResult);

                // Refresh the TableView
                loadCustomerTable();

                // Clear input fields
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer.", e.getMessage());
            }
        }
    }


    @FXML
    void searchCustomerId(ActionEvent event) {
        String searchId = cusIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Customer ID", "Please enter a Customer ID to search.");
            return;
        }

        try {

            CustomerDto foundCustomer = CUSTOMER_MODEL.searchCustomerById(searchId);

            if (foundCustomer != null) {
                // Populate TextFields with found customer data
                populateFields(foundCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Customer Found", "Search Results", "Customer ID: " + searchId + " has been found.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Customer Not Found", "No customer found with ID: " + searchId);
                loadCustomerTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search customer.", e.getMessage());
        }
    }


    @FXML
    void searchCustomerName(ActionEvent event) {
        String searchName = cusNameTxt.getText().trim();
        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Name Provided", "Please enter a Name to search.");
            return;
        }

        try {

            ArrayList<CustomerDto> foundCustomers = CUSTOMER_MODEL.searchCustomersByName(searchName);

            if (!foundCustomers.isEmpty()) {
                ObservableList<CustomerDto> customerList = FXCollections.observableArrayList(foundCustomers);
                customerTable.setItems(customerList);
                showAlert(Alert.AlertType.INFORMATION, "Customers Found", "Search Results", foundCustomers.size() + " customer(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Customers Found", "No customers found with the name: " + searchName);
                loadCustomerTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search customers.", e.getMessage());
        } finally {
            clearFields();
        }
    }

    private void setNextCustomerID() throws SQLException, ClassNotFoundException {
        cusIdTxt.setText(CUSTOMER_MODEL.suggestNextCustomerID());
    }


    private boolean validateInputs() {
        String customerID = cusIdTxt.getText().trim();
        String cusName = cusNameTxt.getText().trim();
        String cusPhone = cusPhoneTxt.getText().trim();
        String cusEmail = cusEmailTxt.getText().trim();
        String cusAddress = cusAddressTxt.getText().trim();
        String cusUserID = cusUserIdLabel.getText().trim();

        if (customerID.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer ID is required.", null);
            return false;
        }

        if (cusName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer Name is required.", null);
            return false;
        }

        if (cusPhone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer Phone Number is required.", null);
            return false;
        }

        if (cusEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer Email is required.", null);
            return false;
        }

        if (cusAddress.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer Address is required.", null);
            return false;
        }

        if (cusUserID.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "User ID is required.", null);
            return false;
        }

        return true;
    }

    @FXML
    void validateEmail(KeyEvent event) {
        String cusEmail = cusEmailTxt.getText().trim();
        validateUtil.isValidEmail(cusEmail, cusEmailTxt);
    }
    @FXML
    void validatePhone(KeyEvent event){
        String cusPhone = cusPhoneTxt.getText().trim();
        validateUtil.isValidPhone(cusPhone, cusPhoneTxt);
    }
    @FXML
    void validateAddress(KeyEvent event) {
        String cusAddress = cusAddressTxt.getText().trim();
        validateUtil.isValidAddress(cusAddress, cusAddressTxt);
    }
    @FXML
    void validateName(KeyEvent event) {
        String cusName = cusNameTxt.getText().trim();
        validateUtil.isValidName(cusName, cusNameTxt);
    }

}
