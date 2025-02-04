package org.gourmetDelight.controller.inventory;

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
import org.gourmetDelight.bo.BOFactory;
import org.gourmetDelight.bo.custom.CustomerBO;
import org.gourmetDelight.bo.custom.SupplierBO;
import org.gourmetDelight.dto.inventory.SupplierDto;
import org.gourmetDelight.util.KeepUser;
import org.gourmetDelight.util.ValidateUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.gourmetDelight.util.Navigations.setInvisible;

public class SuppliersController implements Initializable {

    @FXML
    private JFXButton backToInventoryBtn;

    @FXML
    private TableColumn<SupplierDto, String> supplierIdCol;

    @FXML
    private TableColumn<SupplierDto, String> supEmailCol, suppAddressCol, suppContactPersonCol, suppNameCol, suppPhoneCol, suppUserIdCol;

    @FXML
    private JFXTextField supplierIdTxt, supplierNameTxt, supplierPhoneTxt, supplierEmailTxt, supplierAddressTxt, supplierContactPersonTxt;

    @FXML
    private Label supplierUserIdLabel;

    @FXML
    private JFXButton supplierSaveBtn, supplierUpdateBtn, supplierDeleteBtn, supplierIdSearchBtn, supplierNameSearchBtn, loadAllData, clearTXT;

    @FXML
    private TableView<SupplierDto> suppliersTable;

    @FXML
    private AnchorPane suppliersAnchorPane;

    SupplierBO supplierBO = (SupplierBO) BOFactory.getInstance().getBO(BOFactory.BOType.SUPPLIER);
    private final ValidateUtil validateUtil = new ValidateUtil();

    public SuppliersController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeFocusText();
        loadUserID();
        initializeTableColumns();
        try {
            setNextSupplierId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            loadSupplierTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load supplier data.", e.getMessage());
        }

        supplierUpdateBtn.setDisable(true);
        supplierDeleteBtn.setDisable(true);
    }

    public void changeFocusText(){
        TextField[] textFields = {supplierIdTxt, supplierNameTxt, supplierContactPersonTxt, supplierPhoneTxt, supplierEmailTxt, supplierAddressTxt};

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

    private void initializeTableColumns() {
        supplierIdCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        suppNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        suppContactPersonCol.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        suppPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        supEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        suppAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        suppUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    @FXML
    void loadAllData(ActionEvent event) throws SQLException, ClassNotFoundException {

        loadSupplierTable();
        clearFields();
        setNextSupplierId();

        supplierSaveBtn.setDisable(false);
        supplierUpdateBtn.setDisable(true);
        supplierDeleteBtn.setDisable(true);
        loadUserID();
    }

    private void loadUserID() {
        String currentUserID = KeepUser.getInstance().getUserID();
        supplierUserIdLabel.setText(currentUserID);
    }

    private void loadSupplierTable() throws SQLException, ClassNotFoundException {
        ObservableList<SupplierDto> suppliers = getAllSuppliers();
        suppliersTable.setItems(suppliers);
        suppliersTable.refresh(); // because table did not show data sometimes
    }

    private ObservableList<SupplierDto> getAllSuppliers() throws SQLException, ClassNotFoundException {
        ArrayList<SupplierDto> supplierList = supplierBO.getAll();
        return FXCollections.observableArrayList(supplierList);
    }

    @FXML
    void selectFromTable(MouseEvent event) {
        SupplierDto selectedItem = suppliersTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }


    @FXML
    void saveSupplier(ActionEvent event) {
        try {
            if (validateInputs()) {
                SupplierDto supplier = new SupplierDto(
                        supplierIdTxt.getText().trim(),
                        supplierNameTxt.getText().trim(),
                        supplierContactPersonTxt.getText().trim(),
                        supplierPhoneTxt.getText().trim(),
                        supplierEmailTxt.getText().trim(),
                        supplierAddressTxt.getText().trim(),
                        supplierUserIdLabel.getText().trim()
                );

                boolean result = supplierBO.save(supplier);
                if(result){
                    showAlert(Alert.AlertType.INFORMATION, "Supplier Saved");
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Failed to save supplier");
                }

                loadSupplierTable();
                clearFields();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save supplier.", e.getMessage());
        }
    }

    @FXML
    void updateSupplier(ActionEvent event) {
        try {
            if (validateInputs()) {
                SupplierDto supplier = new SupplierDto(
                        supplierIdTxt.getText().trim(),
                        supplierNameTxt.getText().trim(),
                        supplierContactPersonTxt.getText().trim(),
                        supplierPhoneTxt.getText().trim(),
                        supplierEmailTxt.getText().trim(),
                        supplierAddressTxt.getText().trim(),
                        supplierUserIdLabel.getText().trim()
                );

                boolean result = supplierBO.update(supplier);
                if(result){
                    showAlert(Alert.AlertType.INFORMATION, "Supplier Updated");
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Failed to update supplier");
                }
                loadSupplierTable();
                clearFields();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update supplier.", e.getMessage());
        }
    }

    @FXML
    void deleteSupplier(ActionEvent event) {
        SupplierDto selectedSupplier = suppliersTable.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Supplier Selected", "Please select a supplier to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this supplier?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean deleteResult = supplierBO.delete(selectedSupplier.getSupplierID());
                if(deleteResult){
                    showAlert(Alert.AlertType.INFORMATION, "Supplier Deleted");
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Failed to Delete supplier");
                }

                loadSupplierTable();
                clearFields();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier.", e.getMessage());
            }
        }
    }

    @FXML
    void searchSupplierId(ActionEvent event) {
        try {
            SupplierDto foundSupplier = supplierBO.searchById(supplierIdTxt.getText().trim());
            if (foundSupplier != null) {
                populateFields(foundSupplier);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No Supplier Found", "Supplier not found.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Search Failed", e.getMessage());
        }
    }

        @FXML
    void searchSupplierName(ActionEvent event) {
            String searchName =supplierNameTxt.getText();

            try {

                ArrayList<SupplierDto> foundsuppliers = supplierBO.searchByName(searchName);

                if (!foundsuppliers.isEmpty()) {
                    ObservableList<SupplierDto> supplierList = FXCollections.observableArrayList(foundsuppliers);
                    suppliersTable.setItems(supplierList);
                    showAlert(Alert.AlertType.INFORMATION, "Suppliers Found", "Search Results", foundsuppliers.size() + " Suppliers(s) found with the name: " + searchName);
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "No Results", "No Suppliers Found", "No Suppliers found with the name: " + searchName);
                    loadSupplierTable();
                    clearFields();
                }
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to search suppliers.", e.getMessage());
            } finally {
                clearFields();
            }

    }

    private void populateFields(SupplierDto supplier) {
        supplierIdTxt.setText(supplier.getSupplierID());
        supplierNameTxt.setText(supplier.getName());
        supplierPhoneTxt.setText(supplier.getPhone());
        supplierEmailTxt.setText(supplier.getEmail());
        supplierAddressTxt.setText(supplier.getAddress());
        supplierContactPersonTxt.setText(supplier.getContactPerson());

        supplierSaveBtn.setDisable(true);
        supplierDeleteBtn.setDisable(false);
        supplierUpdateBtn.setDisable(false);

    }

    private void clearFields() {
        supplierIdTxt.clear();
        supplierNameTxt.clear();
        supplierPhoneTxt.clear();
        supplierEmailTxt.clear();
        supplierAddressTxt.clear();
        supplierContactPersonTxt.clear();
    }

    @FXML
    void clearTextfileds(ActionEvent event) {
        clearFields();
    }

    private boolean validateInputs() {

            String supplierId = supplierIdTxt.getText().trim();
            String supplierName = supplierNameTxt.getText().trim();
            String supplierContactPerson = supplierContactPersonTxt.getText().trim();
            String supplierPhone = supplierPhoneTxt.getText().trim();
            String supplierEmail = supplierEmailTxt.getText().trim();
            String supplierAddress = supplierAddressTxt.getText().trim();
            String supplierUserID = supplierUserIdLabel.getText().trim();

            if (supplierId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier ID is required.", null);
                return false;
            }

            if (supplierName.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier Name is required.", null);
                return false;
            }

            if (supplierContactPerson.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Contact Person Name is required.", null);
                return false;
            }

            if (supplierPhone.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier Phone Number is required.", null);
                return false;
            }

            if (supplierEmail.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier Email is required.", null);
                return false;
            }

            if (supplierAddress.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Supplier Address is required.", null);
                return false;
            }

            if (supplierUserID.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "User ID is required.", null);
                return false;
            }

            return true;
    }

    public void setNextSupplierId() throws SQLException, ClassNotFoundException {
        supplierIdTxt.setText(supplierBO.suggestNextID());
    }

        @FXML
    void validateAddress(KeyEvent event) {
            String address = supplierAddressTxt.getText().trim();
            validateUtil.isValidAddress(address, supplierAddressTxt);
    }

    @FXML
    void validateEmail(KeyEvent event) {
        String email = supplierEmailTxt.getText().trim();
        validateUtil.isValidEmail(email, supplierEmailTxt);
    }

    @FXML
    void validateName(KeyEvent event) {
        String Name = supplierNameTxt.getText().trim();
        validateUtil.isValidName(Name, supplierNameTxt);
    }

    @FXML
    void validateContactName(KeyEvent event) {
        String name = supplierContactPersonTxt.getText().trim();
        validateUtil.isValidName(name, supplierContactPersonTxt);
    }


    @FXML
    void validatePhone(KeyEvent event) {
        String phone = supplierPhoneTxt.getText().trim();
        validateUtil.isValidPhone(phone, supplierPhoneTxt);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType,String content) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }

        @FXML
    void backToInventory(ActionEvent event) {
        setInvisible(suppliersAnchorPane);
    }




}
