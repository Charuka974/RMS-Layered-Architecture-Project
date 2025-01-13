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
import javafx.scene.layout.Pane;

import static org.gourmetDelight.model.inventory.InventoryModel.getAllItems;



import org.gourmetDelight.dto.inventory.InventoryItemDto;

import org.gourmetDelight.model.inventory.InventoryModel;

import org.gourmetDelight.util.Navigations;
import org.gourmetDelight.util.ValidateUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;



public class InventoryController implements Initializable {

    @FXML
    private JFXButton ItemIdSearchBtn;

    @FXML
    private JFXButton ItemNameSearchBtn;

    @FXML
    private JFXButton clearTXT;

    @FXML
    private AnchorPane inventoryAnchorPane;

    @FXML
    private JFXTextField inventoryItemIdTxt;

    @FXML
    private TableView<InventoryItemDto> inventoryItemTable;

    @FXML
    private Pane inventoryPane;

    @FXML
    private JFXButton itemDeleteBtn;

    @FXML
    private JFXTextField itemDescTxt;

    @FXML
    private TableColumn<InventoryItemDto, String> itemIdCol, itemNameCol, itemDescriptionCol;

    @FXML
    private TableColumn<InventoryItemDto, Double> itemQtyCol;
    @FXML
    private TableColumn<InventoryItemDto, String>itemUnitCol;

    @FXML
    private JFXTextField itemNameTxt;


    @FXML
    private JFXTextField itemQtyTxt;

    @FXML
    private JFXButton itemSaveBtn;


    @FXML
    private JFXButton itemUpdateBtn;

    @FXML
    private JFXButton loadAllData;

    @FXML
    private JFXButton suppliersBtn;

    @FXML
    private ChoiceBox<String> unitChoiceBox;

    private final InventoryModel INVENTORY_MODEL;
    private final ValidateUtil validateUtil;

    public InventoryController() {
        this.INVENTORY_MODEL = new InventoryModel();
        this.validateUtil = new ValidateUtil();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeFocusText();
        initializeTableColumns();

        try {
            loadItemTable();
            setNextItemId();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error",
                    "Failed to load inventory data.", e.getMessage());
        }


    }

    public void changeFocusText(){
        TextField[] textFields = {inventoryItemIdTxt, itemNameTxt, itemDescTxt, itemQtyTxt};

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

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private void loadItemTable() throws ClassNotFoundException, SQLException {
        ObservableList<InventoryItemDto> itemList = FXCollections.observableArrayList(getAllItems());
        inventoryItemTable.setItems(itemList);
    }


    private void initializeTableColumns() {
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("inventoryItemId"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        itemQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));


    }

    private InventoryItemDto createItemFromFields() {
        return new InventoryItemDto(
                inventoryItemIdTxt.getText().trim(),
                itemNameTxt.getText().trim(),
                itemDescTxt.getText().trim(),
                Double.parseDouble(itemQtyTxt.getText().trim()),
                unitChoiceBox.getValue().trim()
        );
    }


    @FXML
    void loadAllData(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadItemTable();
        clearFields();
        setNextItemId();
        itemSaveBtn.setDisable(false);
    }

    private void setNextItemId() throws SQLException, ClassNotFoundException {
        inventoryItemIdTxt.setText(INVENTORY_MODEL.suggestNextItemID());
    }


    private void clearFields() {
        inventoryItemIdTxt.clear();
        itemNameTxt.clear();
        itemDescTxt.clear();
        itemQtyTxt.clear();
        unitChoiceBox.setValue(null);

    }


    @FXML
    void selectFromTable(MouseEvent event) {
        InventoryItemDto selectedItem = inventoryItemTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    private void populateFields(InventoryItemDto selectedItem) {
        inventoryItemIdTxt.setText(selectedItem.getInventoryItemId());
        itemNameTxt.setText(selectedItem.getName());
        itemDescTxt.setText(selectedItem.getDescription());
        itemQtyTxt.setText(String.valueOf(selectedItem.getQuantity()));
        unitChoiceBox.setValue(selectedItem.getUnit());

        itemSaveBtn.setDisable(true);
    }

    @FXML
    void clearTextfileds(ActionEvent event) {
        clearFields();
    }

    @FXML
    void saveItem(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            InventoryItemDto newItem = createItemFromFields();
            String result = INVENTORY_MODEL.addItem(newItem);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Item Saved", result);
            loadItemTable();
            clearFields();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save item.", e.getMessage());
        }
    }

    @FXML
    void updateItem(ActionEvent event) {
        InventoryItemDto selectedItem = inventoryItemTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update.", null);
            return;
        }

        if (!validateInputs()) return;

        try {
            InventoryItemDto updatedItem = createItemFromFields();
            String result = INVENTORY_MODEL.updateItem(updatedItem);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Item Updated", result);
            loadItemTable();
            clearFields();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update item.", e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (inventoryItemIdTxt.getText().trim().isEmpty() ||
                itemNameTxt.getText().trim().isEmpty() ||
                itemDescTxt.getText().trim().isEmpty() ||
                itemQtyTxt.getText().trim().isEmpty() ||
                unitChoiceBox.getValue().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.", null);
            return false;
        }

        try {
            Double.parseDouble(itemQtyTxt.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Quantity must be a number.", null);
            return false;
        }
        return true;
    }

    @FXML
    void deleteItem(ActionEvent event) {
        InventoryItemDto selectedItem = inventoryItemTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to delete.", null);
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert("Confirm Deletion",
                "Are you sure you want to delete this item from the inventory?",
                "Item ID: " + selectedItem.getInventoryItemId());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String deleteResult = INVENTORY_MODEL.deleteItem(selectedItem.getInventoryItemId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item Deleted", deleteResult);
                loadItemTable();
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete item.", e.getMessage());
            }
        }
    }

    @FXML
    void searchItemId(ActionEvent event) {
        String searchId = inventoryItemIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Item ID", "Please enter a Item ID to search.");
            return;
        }

        try {

            InventoryItemDto foundItem = INVENTORY_MODEL.searchItemById(searchId);

            if (foundItem != null) {
                populateFields(foundItem);
                showAlert(Alert.AlertType.INFORMATION, "Item Found", "Search Results", "Item ID: " + searchId + " has been found.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Item Not Found", "No Item found with ID: " + searchId);
                loadItemTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search Item.", e.getMessage());
        }
    }

    @FXML
    void searchItemName(ActionEvent event) {
        String searchName = itemNameTxt.getText().trim();
        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Item Provided", "Please enter a Item Name to search.");
            return;
        }

        try {

            ArrayList<InventoryItemDto> foundItems = INVENTORY_MODEL.searchItemByName(searchName);

            if (!foundItems.isEmpty()) {
                ObservableList<InventoryItemDto> itemList = FXCollections.observableArrayList(foundItems);
                inventoryItemTable.setItems(itemList);
                showAlert(Alert.AlertType.INFORMATION, "Item Found", "Search Results", foundItems.size() + " Item(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Item Found", "No Items found with the name: " + searchName);
                loadItemTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search items.", e.getMessage());
        } finally {
            clearFields();
        }
    }


    @FXML
    void validateDescription(KeyEvent event) {
        String description = itemDescTxt.getText().trim();
        validateUtil.isValidDescription(description, itemDescTxt);
    }

    @FXML
    void validateName(KeyEvent event) {
        String name = itemNameTxt.getText().trim();
        validateUtil.isValidName(name, itemNameTxt);
    }

    @FXML
    void validateQty(KeyEvent event) {
        String qty = itemQtyTxt.getText().trim();
        validateUtil.isValidQuantity(qty, itemQtyTxt);
    }

    @FXML
    void loadSuppliers(ActionEvent event) throws IOException {
        Navigations navigation = new Navigations();
        navigation.loadPage(inventoryAnchorPane, "/view/inventory/SuppliersPanel.fxml");
    }

}
