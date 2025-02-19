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
import org.gourmetDelight.bo.BOFactory;
import org.gourmetDelight.bo.custom.InventoryItemsBO;
import org.gourmetDelight.bo.custom.MenuItemBO;
import org.gourmetDelight.bo.custom.MenuItemIngredientsBO;
import org.gourmetDelight.dao.custom.MenuItemIngredientsDAO;
import org.gourmetDelight.dao.custom.impl.menuItems.MenuItemIngredientsDAOImpl;
import org.gourmetDelight.dto.menuItems.MenuItemDto;
import org.gourmetDelight.dto.menuItems.MenuItemIngredientsDto;
import org.gourmetDelight.util.ValidateUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;



public class MenuController implements Initializable {

    MenuItemIngredientsBO menuItemIngredientsBO = (MenuItemIngredientsBO) BOFactory.getInstance().getBO(BOFactory.BOType.MENU_ITEM_INGREDIENTS);
    InventoryItemsBO inventoryItemsBO = (InventoryItemsBO) BOFactory.getInstance().getBO(BOFactory.BOType.INVENTORY);

    @FXML
    private TableColumn<MenuItemDto, String> menuItemIdCol;

    @FXML
    private TableColumn<MenuItemDto, String> itemNameCol;

    @FXML
    private TableColumn<MenuItemDto, String> itemDescriptionCol;

    @FXML
    private TableColumn<MenuItemDto, Double> unitPriceCol;

    @FXML
    private TableColumn<MenuItemDto, String> categoryCol;

    @FXML
    private TableColumn<MenuItemDto, String> kitchenSectionCol;

    @FXML
    private JFXButton clearTXT;

    @FXML
    private ChoiceBox<String> itemKitchenSectionTxtCB;

    @FXML
    private JFXTextField menuItemIdTxt, menuItemNameTxt, menuItemDescTxt, menuItemUnitPriceTxt, menuItemCategoryTxt;

    @FXML
    private JFXButton itemSaveBtn, itemUpdateBtn, itemDeleteBtn, menuItemIdSearchBtn, menuItemNameSearchBtn, loadAllData;

    @FXML
    private TableView<MenuItemDto> menuItemTable;

    @FXML
    private AnchorPane menuAnchorPane;

    MenuItemBO menuItemBO = (MenuItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.MENU_ITEM);
    private final ValidateUtil validateUtil = new ValidateUtil();

    public MenuController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setNextMenuItemID();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        initializeTableColumns();

        try {
            loadMenuItemsTable();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load menu item data.", e.getMessage());
        }


        itemUpdateBtn.setDisable(true);
        itemDeleteBtn.setDisable(true);

        try {
            loadMenuItemIngredients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        changeFocusText();
    }

    public void changeFocusText() {
        // Add all relevant TextFields to the array
        TextField[] textFields = {
                menuItemIdTxt, menuItemNameTxt, menuItemDescTxt, menuItemUnitPriceTxt, menuItemCategoryTxt,inventoryItemNameTxt, inventoryItemIdTxt, inventoryItemQtyTxt
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
    void selectFromTable(MouseEvent event) throws SQLException, ClassNotFoundException {
        MenuItemDto selectedItem = menuItemTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    private void initializeTableColumns() {
        menuItemIdCol.setCellValueFactory(new PropertyValueFactory<>("menuItemID"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        kitchenSectionCol.setCellValueFactory(new PropertyValueFactory<>("kitchenSection"));
    }

    private void loadMenuItemsTable() throws ClassNotFoundException, SQLException {
        ObservableList<MenuItemDto> menuItems = getAllMenuItems();
        menuItemTable.setItems(menuItems);
    }

    private ObservableList<MenuItemDto> getAllMenuItems() throws ClassNotFoundException, SQLException {
        ArrayList<MenuItemDto> itemList = menuItemBO.getAll();
        return FXCollections.observableArrayList(itemList);
    }

    private void populateFields(MenuItemDto item) throws SQLException, ClassNotFoundException {
        menuItemIdTxt.setText(item.getMenuItemID());
        menuItemNameTxt.setText(item.getName());
        menuItemDescTxt.setText(item.getDescription());
        menuItemUnitPriceTxt.setText(String.valueOf(item.getPrice()));
        menuItemCategoryTxt.setText(item.getCategory());
        itemKitchenSectionTxtCB.setValue(item.getKitchenSection());

        itemSaveBtn.setDisable(true);
        itemUpdateBtn.setDisable(false);
        itemDeleteBtn.setDisable(false);

        loadIngredientsItemsTable(item.getMenuItemID());
    }

    private void clearFields() {
        menuItemIdTxt.clear();
        menuItemNameTxt.clear();
        menuItemDescTxt.clear();
        menuItemUnitPriceTxt.clear();
        menuItemCategoryTxt.clear();
        itemKitchenSectionTxtCB.setValue(null);

        itemUpdateBtn.setDisable(true);
        itemDeleteBtn.setDisable(true);
        itemSaveBtn.setDisable(false);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void saveItem(ActionEvent event) {
        try {
            if (validateInputs()) {
                MenuItemDto newItem = new MenuItemDto(
                        menuItemIdTxt.getText().trim(),
                        menuItemNameTxt.getText().trim(),
                        menuItemDescTxt.getText().trim(),
                        Double.parseDouble(menuItemUnitPriceTxt.getText().trim()),
                        menuItemCategoryTxt.getText().trim(),
                        itemKitchenSectionTxtCB.getValue().trim()
                );

                boolean result = menuItemBO.save(newItem);
                if (result){
                    showAlert("Successfully saved");
                }else{
                    showAlert("Failed to save item");
                }

                loadMenuItemsTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save menu item.", e.getMessage());
        }
    }

    @FXML
    void updateItem(ActionEvent event) {
        MenuItemDto selectedItem = menuItemTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Menu Item Selected", "Please select a menu item to update.");
            return;
        }

        try {
            if (validateInputs()) {
                MenuItemDto updatedItem = new MenuItemDto(
                        menuItemIdTxt.getText().trim(),
                        menuItemNameTxt.getText().trim(),
                        menuItemDescTxt.getText().trim(),
                        Double.parseDouble(menuItemUnitPriceTxt.getText().trim()),
                        menuItemCategoryTxt.getText().trim(),
                        itemKitchenSectionTxtCB.getValue().trim()
                );

                boolean result = menuItemBO.update(updatedItem);
                if (result){
                    showAlert("Successfully updated menu item");
                }else{
                    showAlert("Failed to updated menu item");
                }

                loadMenuItemsTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update menu item.", e.getMessage());
        }
    }

    @FXML
    void deleteItem(ActionEvent event) {
        MenuItemDto selectedItem = menuItemTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Menu Item Selected", "Please select a menu item to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this menu item?");
        confirmationAlert.setContentText("Menu Item ID: " + selectedItem.getMenuItemID());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleteResult = menuItemBO.delete(selectedItem.getMenuItemID());
                if (deleteResult){
                    showAlert("Successfully deleted menu item");
                }else{
                    showAlert("Failed to delete menu item");
                }

                loadMenuItemsTable();
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete menu item.", e.getMessage());
            }
        }
    }

    @FXML
    void loadAllData(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            loadMenuItemsTable();
            clearFields();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load all menu items.", e.getMessage());
        }
        itemSaveBtn.setDisable(false);
        itemUpdateBtn.setDisable(true);
        itemDeleteBtn.setDisable(true);
        setNextMenuItemID();
    }

    private boolean validateInputs() {
        if (menuItemIdTxt.getText().trim().isEmpty() || menuItemNameTxt.getText().trim().isEmpty() ||
                menuItemDescTxt.getText().trim().isEmpty() || menuItemUnitPriceTxt.getText().trim().isEmpty() ||
                itemKitchenSectionTxtCB.getValue().trim().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Validation Error", null, "All fields are required.");
            return false;
        }
        return true;
    }

    @FXML
    void validateName(KeyEvent event) {
        validateUtil.isValidName(menuItemNameTxt.getText().trim(), menuItemNameTxt);
    }

    @FXML
    void validatePrice(KeyEvent event) {
        validateUtil.isValidPrice(menuItemUnitPriceTxt.getText().trim(), menuItemUnitPriceTxt);
    }


    @FXML
    void validateCategory(KeyEvent event) {
        validateUtil.isValidName(menuItemCategoryTxt.getText().trim(), menuItemCategoryTxt);
    }

    @FXML
    void validateDescription(KeyEvent event) {
        validateUtil.isValidName(menuItemDescTxt.getText().trim(), menuItemDescTxt);
    }


    @FXML
    void clearTextfileds(ActionEvent event) {
        clearFields();
        itemSaveBtn.setDisable(true);

    }


    @FXML
    void searchItemName(ActionEvent event) {
        String searchName = menuItemNameTxt.getText().trim();
        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Name Provided", "Please enter a Menu Item Name to search.");
            return;
        }

        try {
            // Search for menu items by name
            ArrayList<MenuItemDto> foundMenuItems = menuItemBO.searchByName(searchName);

            if (!foundMenuItems.isEmpty()) {
                ObservableList<MenuItemDto> menuItemList = FXCollections.observableArrayList(foundMenuItems);
                menuItemTable.setItems(menuItemList);
                showAlert(Alert.AlertType.INFORMATION, "Menu Items Found", "Search Results", foundMenuItems.size() + " menu item(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Menu Items Found", "No menu items found with the name: " + searchName);
                loadMenuItemsTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search menu items.", e.getMessage());
        }
    }

    @FXML
    void searchmenuItemId(ActionEvent event) {
        String searchId = menuItemIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Menu Item ID Provided", "Please enter a Menu Item ID to search.");
            return;
        }

        try {
            // Search for menu item by ID
            MenuItemDto foundMenuItem = menuItemBO.searchById(searchId);

            if (foundMenuItem != null) {
                // Populate TextFields with found menu item data
                populateFields(foundMenuItem);
                showAlert(Alert.AlertType.INFORMATION, "Menu Item Found", "Search Results", "Menu Item ID: " + searchId + " has been found.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Menu Item Not Found", "No menu item found with ID: " + searchId);
                loadMenuItemsTable();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search menu item.", e.getMessage());
        }
    }

    private void setNextMenuItemID() throws SQLException, ClassNotFoundException {
        // Set the next Menu Item ID by calling the method from your model
        menuItemIdTxt.setText(menuItemBO.suggestNextID());
    }





// --------------------------- recipe controller ------------------------------


    @FXML
    private JFXButton clearTXTReciepe;

    @FXML
    private TableColumn<MenuItemIngredientsDto, String> ingredientsMenuItemIDCol;

    @FXML
    private TableColumn<MenuItemIngredientsDto, Double> ingredientsQtyCol;

    @FXML
    private TableView<MenuItemIngredientsDto> ingredientsTable;

    @FXML
    private TableColumn<MenuItemIngredientsDto, String> ingredientsTableInventoryIdCol;

    @FXML
    private JFXButton inventoryItemIdSearchBtn;

    @FXML
    private JFXTextField inventoryItemIdTxt;

    @FXML
    private JFXButton inventoryItemNameSearchBtn;

    @FXML
    private JFXTextField inventoryItemNameTxt;

    @FXML
    private JFXTextField inventoryItemQtyTxt;

    @FXML
    private JFXButton itemDeleteBtnReciepe;

    @FXML
    private Label itemUnitsLbl;

    @FXML
    private JFXButton itemSaveBtnReciepe;

    @FXML
    private JFXButton itemUpdateBtnReciepe;


    @FXML
    void searchInventoryItemName(ActionEvent event) throws SQLException, ClassNotFoundException {
        // we can search the item by name first and then search it by id so that we can get all menu  items made with that item
        itemUnitsLbl.setText(inventoryItemsBO.getItemUnitsByName(inventoryItemNameTxt.getText()));
        try {
            String itemId = inventoryItemsBO.searchInventoryItemName(inventoryItemNameTxt.getText().trim());
            if (itemId == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Inventory Item Not Found", "The item you are searching for could not be found.");
                inventoryItemIdTxt.setText(null);
            }else{
                inventoryItemIdTxt.setText(itemId);
            }

        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to get Inventory Item ID.", e.getMessage());
        }
    }

    @FXML
    void clearTextfiledsReciepe(ActionEvent event) {
        inventoryItemIdTxt.clear();
        inventoryItemQtyTxt.clear();
        inventoryItemNameTxt.clear();

    }



    // Load the table and disable buttons initially
    private void loadMenuItemIngredients() throws SQLException, ClassNotFoundException {
        initializeIngredientsTableColumns();
        loadIngredientsItemsTable(menuItemIdTxt.getText());
        itemUpdateBtnReciepe.setDisable(true);
        itemDeleteBtnReciepe.setDisable(true);
    }

    // Initialize table columns
    private void initializeIngredientsTableColumns() {
        ingredientsMenuItemIDCol.setCellValueFactory(new PropertyValueFactory<>("menuItemID"));
        ingredientsTableInventoryIdCol.setCellValueFactory(new PropertyValueFactory<>("inventoryItemID"));
        ingredientsQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantityNeeded"));
    }

    // Load data into the table
    private void loadIngredientsItemsTable(String menuItemID) throws SQLException, ClassNotFoundException {
        ObservableList<MenuItemIngredientsDto> recipeItems = FXCollections.observableArrayList(menuItemIngredientsBO.getAll(menuItemID));
        ingredientsTable.setItems(recipeItems);
    }

    // Save a new ingredient item
    @FXML
    void saveItemReciepe(ActionEvent event) {
        String menuItemID = menuItemIdTxt.getText();
        try {

            String inventoryItemID = inventoryItemIdTxt.getText();
            double quantityNeeded = Double.parseDouble(inventoryItemQtyTxt.getText());

            MenuItemIngredientsDto dto = new MenuItemIngredientsDto(menuItemID, inventoryItemID, quantityNeeded);
            boolean result = menuItemIngredientsBO.save(dto);

            if (result){
                showAlert("Successfully saved");
            }else{
                showAlert("Failed to save");
            }
            loadIngredientsItemsTable(menuItemID);
            clearTextFieldsReciepe(null);
            menuItemIdTxt.setText(menuItemID);
        } catch (Exception e) {
            showAlert("Failed to save: " + e.getMessage());
        }
    }

    // Update an existing ingredient item
    @FXML
    void updateItemReciepe(ActionEvent event) {
        String menuItemID = menuItemIdTxt.getText();
        try {

            String inventoryItemID = inventoryItemIdTxt.getText();
            double quantityNeeded = Double.parseDouble(inventoryItemQtyTxt.getText());

            MenuItemIngredientsDto dto = new MenuItemIngredientsDto(menuItemID, inventoryItemID, quantityNeeded);
            boolean result = menuItemIngredientsBO.update(dto);

            if (result){
                showAlert("Successfully updated");
            }else{
                showAlert("Failed to update");
            }
            loadIngredientsItemsTable(menuItemID);
            clearTextFieldsReciepe(null);
            menuItemIdTxt.setText(menuItemID);
        } catch (Exception e) {
            showAlert("Failed to update: " + e.getMessage());
        }
    }

    // Delete an ingredient item
    @FXML
    void deleteItemReciepe(ActionEvent event) {
        String menuItemID = null;
        try {
            menuItemID = menuItemIdTxt.getText();
            String inventoryItemID = inventoryItemIdTxt.getText();

            String result = menuItemIngredientsBO.delete(menuItemID, inventoryItemID);

            showAlert(result);
            loadIngredientsItemsTable(menuItemID);
            clearTextFieldsReciepe(null);
            menuItemIdTxt.setText(menuItemID);
        } catch (Exception e) {
            showAlert("Failed to delete: " + e.getMessage());
        }
    }

    // Select an item from the table and populate the text fields
    @FXML
    void selectFromTheReciepeTable(MouseEvent event) throws SQLException, ClassNotFoundException {
        MenuItemIngredientsDto selectedItem = ingredientsTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            //menuItemIdTxt.setText(selectedItem.getMenuItemID());
            inventoryItemIdTxt.setText(selectedItem.getInventoryItemID());
            inventoryItemQtyTxt.setText(String.valueOf(selectedItem.getQuantityNeeded()));
            itemUnitsLbl.setText(inventoryItemsBO.searchById(selectedItem.getInventoryItemID()).getUnit());
            inventoryItemNameTxt.setText(inventoryItemsBO.searchById(selectedItem.getInventoryItemID()).getName());

            itemUpdateBtnReciepe.setDisable(false);
            itemDeleteBtnReciepe.setDisable(false);
        }
    }


    @FXML
    void searchInventoryItemId(ActionEvent event) throws SQLException, ClassNotFoundException {
        itemUnitsLbl.setText(inventoryItemsBO.searchById(inventoryItemIdTxt.getText()).getUnit());
        inventoryItemNameTxt.setText(inventoryItemsBO.searchById(inventoryItemIdTxt.getText()).getName());

        try {
            String inventoryItemID = inventoryItemIdTxt.getText();
            ObservableList<MenuItemIngredientsDto> result = FXCollections.observableArrayList(
                    menuItemIngredientsBO.searchIngredientsByInventoryItemID(inventoryItemID)

            );
            ingredientsTable.setItems(result);
        } catch (Exception e) {
            showAlert("Search failed: " + e.getMessage());
        }
    }


    @FXML
    void clearTextFieldsReciepe(ActionEvent event) {
        menuItemIdTxt.clear();
        inventoryItemIdTxt.clear();
        inventoryItemQtyTxt.clear();
        itemUpdateBtnReciepe.setDisable(true);
        itemDeleteBtnReciepe.setDisable(true);
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





}
