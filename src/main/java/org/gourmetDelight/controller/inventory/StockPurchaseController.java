package org.gourmetDelight.controller.inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.gourmetDelight.bo.custom.InventoryItemsBO;
import org.gourmetDelight.bo.custom.PurchaseBO;
import org.gourmetDelight.bo.custom.SupplierBO;
import org.gourmetDelight.bo.custom.impl.InventoryItemsBOImpl;
import org.gourmetDelight.bo.custom.impl.PurchaseBOImpl;
import org.gourmetDelight.bo.custom.impl.SupplierBOImpl;
import org.gourmetDelight.dto.inventory.InventoryItemDto;
import org.gourmetDelight.dto.inventory.SupplierDto;
import org.gourmetDelight.entity.StockPurchase;
import org.gourmetDelight.entity.StockPurchaseItems;
import org.gourmetDelight.dto.custom.StockPurchaseDTOCustom;
import org.gourmetDelight.util.DateAndTime;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.gourmetDelight.util.Navigations.newWindowPopUp;


public class StockPurchaseController implements Initializable {

    @FXML
    private JFXButton addNewItem;

    @FXML
    private JFXButton addNewSupplierBtn;

    @FXML
    private JFXButton clearTXTBtn;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, LocalDate> dateCol;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, String> itemIdCol;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, String> statusCol;

    @FXML
    private JFXTextField itemIdTxt;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, String> itemNameCol;

    @FXML
    private JFXTextField itemNameTxt;

    @FXML
    private JFXButton itemSearchBtn;

    @FXML
    private JFXButton loadAllDataBtn;

    @FXML
    private JFXTextField purchaseDateTxt;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, String> purchaseIdCol;

    @FXML
    private JFXButton purchaseIdSearchBtn;

    @FXML
    private JFXTextField purchaseIdTxt;

    @FXML
    private ChoiceBox<String> purchaseStatusChoiceBox;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, Double> quantityCol;

    @FXML
    private JFXTextField quantityTxt;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private TableView<StockPurchaseDTOCustom> stockPurchaseTable;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, String> supplierIdCol;

    @FXML
    private JFXTextField supplierIdTxt;

    @FXML
    private JFXTextField supplierNameTxt;

    @FXML
    private JFXButton supplierSearchBtn;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, Double> totalAmountCol;

    @FXML
    private JFXTextField totalAmountTxt;

    @FXML
    private JFXTextField unitPerPriceTxt;

    @FXML
    private TableColumn<StockPurchaseDTOCustom, Double> unitPriceCol;

    @FXML
    private JFXTextField unitPriceTxt;

    @FXML
    private Label unitTxt;

    @FXML
    private JFXButton updateBtn;

    InventoryItemsBO inventoryItemsBO = new InventoryItemsBOImpl();
    SupplierBO supplierBO = new SupplierBOImpl();
    PurchaseBO purchaseBO = new PurchaseBOImpl();
    DateAndTime dateAndTime = new DateAndTime();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTable();
        purchaseDateTxt.setText(dateAndTime.addDate());
        setNextPurchaseID();
        try {
            loadStockPurchaseTable();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load purchase data.", e.getMessage());
        }
    }

    private void loadStockPurchaseTable() throws SQLException, ClassNotFoundException {
        ObservableList<StockPurchaseDTOCustom> purchaseList = purchaseBO.getAllStockPurchases();
        stockPurchaseTable.setItems(purchaseList);
        setNextPurchaseID();
    }

    private void setNextPurchaseID() {
        try {
            String nextID = purchaseBO.suggestNextID();
            purchaseIdTxt.setText(nextID);
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate next Purchase ID.", e.getMessage());
        }
    }

    private void initializeTable() {
        purchaseIdCol.setCellValueFactory(new PropertyValueFactory<>("purchaseID"));
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        supplierIdCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
    }

    @FXML
    void selectFromTable(MouseEvent event) {
        StockPurchaseDTOCustom selectedItem = stockPurchaseTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    private void populateFields(StockPurchaseDTOCustom selectedItem) {
        purchaseIdTxt.setText(selectedItem.getPurchaseID());
        itemIdTxt.setText(selectedItem.getItemID());
        itemNameTxt.setText(selectedItem.getItemName());
        supplierIdTxt.setText(selectedItem.getSupplierID());
        quantityTxt.setText(String.valueOf(selectedItem.getQuantity()));
        unitPriceTxt.setText(String.valueOf(selectedItem.getUnitPrice()));
        totalAmountTxt.setText(String.valueOf(selectedItem.getTotalAmount()));
        purchaseDateTxt.setText(selectedItem.getPurchaseDate().toString());
        purchaseStatusChoiceBox.setValue(selectedItem.getStatus());

        supplierNameTxt.setText(selectedItem.getSupplierName());
        unitTxt.setText(selectedItem.getUnitsMeasured());
        unitPerPriceTxt.setText(String.valueOf(selectedItem.getUnit()));

    }

    @FXML
    void addNewItem(ActionEvent event) throws IOException {
        newWindowPopUp("Add New Customer", "/view/inventory/InventoryPanel.fxml");
    }

    @FXML
    void addNewSupplier(ActionEvent event) throws IOException {
        newWindowPopUp("Add New Customer", "/view/inventory/SuppliersPanel.fxml");
    }


    void clearTextfields() {
        purchaseIdTxt.clear();
        purchaseDateTxt.clear();
        itemIdTxt.clear();
        itemNameTxt.clear();
        supplierIdTxt.clear();
        supplierNameTxt.clear();
        quantityTxt.clear();
        purchaseStatusChoiceBox.setValue(null);
        unitTxt.setText("Units");
        unitPerPriceTxt.clear();
        totalAmountTxt.clear();
        unitPriceTxt.clear();
    }

    @FXML
    void deletePurchase(ActionEvent event) {
        String purchaseID = purchaseIdTxt.getText();
        try {
            String result = purchaseBO.deletePurchase(purchaseID);
            showAlert(Alert.AlertType.INFORMATION, "Delete Result", null, result);
            loadStockPurchaseTable();
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete purchase.", e.getMessage());
        }
    }

    @FXML
    void loadAllData(ActionEvent event) {
        clearTextfields();
        setNextPurchaseID();
        purchaseDateTxt.setText(dateAndTime.addDate());
        try {
            loadStockPurchaseTable();
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load all data.", e.getMessage());
        }
    }

    @FXML
    void savePurchase(ActionEvent event) {
        String purchaseID = purchaseIdTxt.getText();
        String itemID = itemIdTxt.getText();
        String itemName = itemNameTxt.getText();
        String supplierID = supplierIdTxt.getText();
        double unitPrice = Double.parseDouble(unitPriceTxt.getText());
        double quantity = Double.parseDouble(quantityTxt.getText());
        double totalAmount = unitPrice * quantity;
        LocalDate purchaseDate = LocalDate.parse(purchaseDateTxt.getText());
        String status = purchaseStatusChoiceBox.getValue();
        double unitPerPrice = Double.parseDouble(unitPerPriceTxt.getText());

        StockPurchaseDTOCustom purchase = new StockPurchaseDTOCustom(purchaseID, itemID, itemName, unitPrice, quantity, totalAmount, purchaseDate, supplierID, status, "", 0, "");

        StockPurchaseItems stockPurchaseItemsDto = new StockPurchaseItems(itemID, purchaseID, unitPerPrice, unitPrice, quantity, status);
        StockPurchase stockPurchaseDto = new StockPurchase(purchaseID, supplierID, totalAmount, purchaseDate);

        try {
            if(purchaseBO.save(stockPurchaseDto, stockPurchaseItemsDto)) {
                showAlert(Alert.AlertType.INFORMATION, "Save Result", "Successful", "Successfully saved stock purchase.");
                loadStockPurchaseTable();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save purchase.", e.getMessage());
        }
    }

    @FXML
    void searchItem(ActionEvent event) throws SQLException, ClassNotFoundException {
        String itemName = itemNameTxt.getText();

        ArrayList<InventoryItemDto> items = inventoryItemsBO.searchByName(itemName);

        if (!items.isEmpty()) {
            itemIdTxt.setText(items.get(0).getInventoryItemId());
            unitTxt.setText(items.get(0).getUnit());
            itemNameTxt.setText(items.get(0).getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Inventory Item not found", "Inventory Item not found");
        }

    }

    @FXML
    void searchPurchaseID(ActionEvent event) throws SQLException, ClassNotFoundException {
        String purchaseID = purchaseIdTxt.getText();
        if (purchaseID == null || purchaseID.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Please enter a Purchase ID.");
            return;
        }

        StockPurchaseDTOCustom purchase = purchaseBO.searchByIdReturnTM(purchaseID);

        if (purchase == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Purchase not found", "Purchase not found with the given ID.");
        } else {
            populateFields(purchase);
            showAlert(Alert.AlertType.INFORMATION, "Successfully Found", "Purchase found", "Purchase found with the given ID.");
        }
    }

    @FXML
    void searchSupplier(ActionEvent event) throws SQLException, ClassNotFoundException {
        String name = supplierNameTxt.getText();

        ArrayList<SupplierDto> supplier = supplierBO.searchByName(name);

        if (!supplier.isEmpty()) {
            supplierIdTxt.setText(supplier.get(0).getSupplierID());
            supplierNameTxt.setText(supplier.get(0).getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Supplier not found", "Supplier not found");
        }
    }

    @FXML
    void updatePurchase(ActionEvent event) {
        String purchaseID = purchaseIdTxt.getText();
        String itemID = itemIdTxt.getText();
        String itemName = itemNameTxt.getText();
        String supplierID = supplierIdTxt.getText();
        double unitPrice = Double.parseDouble(unitPriceTxt.getText());
        double quantity = Double.parseDouble(quantityTxt.getText());
        double totalAmount = unitPrice * quantity;
        LocalDate purchaseDate = LocalDate.parse(purchaseDateTxt.getText());
        String status = purchaseStatusChoiceBox.getValue();
        double unitPerPrice = Double.parseDouble(unitPerPriceTxt.getText());

        StockPurchaseDTOCustom purchase = new StockPurchaseDTOCustom(purchaseID, itemID, itemName, unitPrice, quantity, totalAmount, purchaseDate, supplierID, status, "", 0, "");

        StockPurchaseItems stockPurchaseItemsDto = new StockPurchaseItems(itemID, purchaseID, unitPerPrice, unitPrice, quantity, status);
        StockPurchase stockPurchaseDto = new StockPurchase(purchaseID, supplierID, totalAmount, purchaseDate);

        try {
            if(purchaseBO.updateStockPurchase(stockPurchaseDto, stockPurchaseItemsDto)) {
                showAlert(Alert.AlertType.INFORMATION, "Update Result", "Successful", "Successfully updated stock purchase.");
                loadStockPurchaseTable();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update purchase.", e.getMessage());
        }



    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header != null && !header.isEmpty() ? header : null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void clearTextfileds(ActionEvent event) {
        clearTextfields();
    }





}