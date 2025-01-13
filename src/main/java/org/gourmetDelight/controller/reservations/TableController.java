package org.gourmetDelight.controller.reservations;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.dto.reservations.TablesDto;
import org.gourmetDelight.model.reservations.TableModel;
import org.gourmetDelight.util.Navigations;
import org.gourmetDelight.util.ValidateUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

//import static org.gourmetDelight.util.Navigations.newWindowPopUp;
//import static org.gourmetDelight.util.Navigations.newWindowPopUpWithObject;

public class TableController implements Initializable {

    @FXML
    private JFXButton reservationsBtn;

    @FXML
    private TableColumn<TablesDto, String> tableIdCol;

    @FXML
    private TableColumn<TablesDto, String> tableNumberCol;

    @FXML
    private TableColumn<TablesDto, Integer> capacityCol;

    @FXML
    private TableColumn<TablesDto, String> locationCol;

    @FXML
    private TableColumn<TablesDto, String> statusCol;

    @FXML
    private JFXTextField tableIdTxt;

    @FXML
    private JFXTextField tableNumberTxt;

    @FXML
    private JFXButton tableIdSearchBtn;


    @FXML
    private JFXButton tableNumberSearchBtn;

    @FXML
    private JFXTextField tableCapacityTxt;

    @FXML
    private JFXTextField locationTxt;

    @FXML
    private ChoiceBox<String> tableStatusChoice;

    @FXML
    private TableView<TablesDto> tablesTable;

    @FXML
    private JFXButton tableSaveBtn, tableUpdateBtn, tableDeleteBtn, loadAllData, clearTXT;

    @FXML
    private JFXButton searchAvailableBtn;

    private final TableModel TABLE_MODEL = new TableModel();
    private final ValidateUtil validateUtil = new ValidateUtil();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();

        try {
            loadTableData();
            suggestNextTableID();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load table data.", e.getMessage());
        }

        tableUpdateBtn.setDisable(true);
        tableDeleteBtn.setDisable(true);
        reservationsBtn.setDisable(true);
    }

    private void initializeTable() {
        tableIdCol.setCellValueFactory(new PropertyValueFactory<>("tableID"));
        tableNumberCol.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

//    private void loadStatusChoices() {
//        tableStatusChoice.setItems(FXCollections.observableArrayList("Available", "Occupied", "Reserved"));
//    }

    @FXML
    void loadAllData(ActionEvent event) {
        try {
            loadTableData();
            clearFields();
            suggestNextTableID();
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data.", e.getMessage());
        }
    }

    private void loadTableData() throws ClassNotFoundException, SQLException {
        ObservableList<TablesDto> tableList = FXCollections.observableArrayList(TABLE_MODEL.getAll());
        tablesTable.setItems(tableList);
    }

    @FXML
    void saveTable(ActionEvent event) {
        try {
            if (validateInputs()) {
                TablesDto table = new TablesDto(
                        tableIdTxt.getText().trim(),
                        tableNumberTxt.getText().trim(),
                        Integer.parseInt(tableCapacityTxt.getText().trim()),
                        locationTxt.getText().trim(),
                        tableStatusChoice.getValue()
                );

                String result = TABLE_MODEL.saveTable(table);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Table Saved", result);
                loadTableData();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save table.", e.getMessage());
        }
    }

    @FXML
    void updateTable(ActionEvent event) {
        TablesDto selectedTable = tablesTable.getSelectionModel().getSelectedItem();
        if (selectedTable == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Table Selected", "Please select a table to update.");
            return;
        }

        try {
            if (validateInputs()) {
                TablesDto updatedTable = new TablesDto(
                        tableIdTxt.getText().trim(),
                        tableNumberTxt.getText().trim(),
                        Integer.parseInt(tableCapacityTxt.getText().trim()),
                        locationTxt.getText().trim(),
                        tableStatusChoice.getValue()
                );

                String result = TABLE_MODEL.updateTable(updatedTable);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Table Updated", result);
                loadTableData();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update table.", e.getMessage());
        }
    }

    @FXML
    void deleteTable(ActionEvent event) {
        TablesDto selectedTable = tablesTable.getSelectionModel().getSelectedItem();
        if (selectedTable == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Table Selected", "Please select a table to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this table?");
        confirmation.setContentText("Table ID: " + selectedTable.getTableID());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String deleteResult = TABLE_MODEL.deleteTable(selectedTable.getTableID());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Table Deleted", deleteResult);
                loadTableData();
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete table.", e.getMessage());
            }
        }
    }

    @FXML
    void searchTableId(ActionEvent event) {
        String searchId = tableIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Table ID", "Please enter a Table ID to search.");
            return;
        }

        try {
            TablesDto foundTable = TABLE_MODEL.searchTableById(searchId);
            if (foundTable != null) {
                populateFields(foundTable);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Table Not Found", "No table found with ID: " + searchId);
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search table.", e.getMessage());
        }
    }

    private void populateFields(TablesDto table) {
        tableIdTxt.setText(table.getTableID());
        tableNumberTxt.setText(table.getTableNumber());
        tableCapacityTxt.setText(String.valueOf(table.getCapacity()));
        locationTxt.setText(table.getLocation());
        tableStatusChoice.setValue(table.getStatus());

        tableSaveBtn.setDisable(true);
        tableUpdateBtn.setDisable(false);
        tableDeleteBtn.setDisable(false);
        checkIfAvailable();
    }

    private void clearFields() {
        tableIdTxt.clear();
        tableNumberTxt.clear();
        tableCapacityTxt.clear();
        locationTxt.clear();
        tableStatusChoice.setValue(null);

        tableSaveBtn.setDisable(false);
        tableUpdateBtn.setDisable(true);
        tableDeleteBtn.setDisable(true);
    }

    private boolean validateInputs() {
        if (tableIdTxt.getText().trim().isEmpty() || tableNumberTxt.getText().trim().isEmpty() ||
                tableCapacityTxt.getText().trim().isEmpty() || locationTxt.getText().trim().isEmpty() ||
                tableStatusChoice.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.", null);
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void loadReservations(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reservations/ReservationPanel.fxml"));
        AnchorPane pane = loader.load();

        // Get the controller and pass the Table ID
        ReservationController reservationController = loader.getController();
        String tableId = tableIdTxt.getText().trim();
        reservationController.setTableId(tableId);

        Navigations navigation = new Navigations();

        // Open the new window with the passed controller
        navigation.newWindowPopUpWithData("Reservations", loader);

    }


    @FXML
    void searchAvailableTables(ActionEvent event) throws SQLException, ClassNotFoundException {

        try {
            ArrayList<TablesDto> foundTables = TABLE_MODEL.searchTablesByAvailability("Available");

            if (!foundTables.isEmpty()) {
                ObservableList<TablesDto> tableList = FXCollections.observableArrayList(foundTables);
                tablesTable.setItems(tableList);
                showAlert(Alert.AlertType.INFORMATION, "Tables Found", "Search Results", foundTables.size() + " Tables are " + "Available");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Tables Found", "No Tables are " + "Available");
                loadTableData();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search available tables.", e.getMessage());
        } finally {
            clearFields();
        }

    }


    @FXML
    void clearTextFields(ActionEvent event) {
        clearFields();

    }


    @FXML
    void searchTableNumber(ActionEvent event) {

    }

    @FXML
    void selectFromTable(MouseEvent event) {
        TablesDto selectedItem = tablesTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    private void checkIfAvailable() {
        if(tableStatusChoice.getValue().equals("Available")){
            reservationsBtn.setDisable(false);
        }else{
            reservationsBtn.setDisable(true);
        }
    }

    @FXML
    void validateNumber(KeyEvent event) {

    }

    @FXML
    void validateTableNumber(KeyEvent event) {

    }

    @FXML
    void validateText(KeyEvent event) {

    }

    private void suggestNextTableID() throws SQLException, ClassNotFoundException {
        tableIdTxt.setText(TABLE_MODEL.suggestNextTableID());
    }


}
