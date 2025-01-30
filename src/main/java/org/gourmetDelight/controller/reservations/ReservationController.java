package org.gourmetDelight.controller.reservations;

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
import org.gourmetDelight.bo.custom.CustomerBO;
import org.gourmetDelight.bo.custom.ReservationBO;
import org.gourmetDelight.bo.custom.impl.CustomerBOImpl;
import org.gourmetDelight.bo.custom.impl.ReservationBOImpl;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.dto.reservations.ReservationDto;
import org.gourmetDelight.dao.custom.impl.CustomerDAOImpl;
import org.gourmetDelight.util.DateAndTime;
import org.gourmetDelight.util.ValidateUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.gourmetDelight.util.Navigations.newWindowPopUp;

public class ReservationController implements Initializable {


    @FXML
    private JFXButton addNewCustomerBtn;

    @FXML
    private JFXButton customerReservationSearchBtn;

    @FXML
    private JFXTextField cusNameTxt;

    @FXML
    private AnchorPane reservationsAnchorPane;

    @FXML
    private Label tableIdLabel;

    @FXML
    private TableView<ReservationDto> reservationTable;

    @FXML
    private TableColumn<ReservationDto, String> resrvationIdCol, cusIdCol, specialRequestsCol, statusCol;

    @FXML
    private TableColumn<ReservationDto, LocalDate> reservationDateCol;

    @FXML
    private TableColumn<ReservationDto, Integer> capacityCol;

    @FXML
    private JFXTextField reservationIdTxt, cusIdTxt, reservationDateTxt, capacityTxt, specialReqTxt;

    @FXML
    private ChoiceBox<String> reservationStatusChoice;

    @FXML
    private DatePicker assignmentDateTxt;

    @FXML
    private JFXTextField assignmentTimeTxt;

    @FXML
    private TableColumn<ReservationDto, String> tableIdCol; // New column for Table ID


    @FXML
    private JFXButton resSaveBtn, resUpdateBtn, resDeleteBtn, loadAllData, clearTXT, reservationIdSearchBtn, cusNameSearchBtn;

    ReservationBO reservationBO = new ReservationBOImpl();
    private final ValidateUtil validateUtil = new ValidateUtil();
    DateAndTime dateAndTime = new DateAndTime();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        //loadStatusChoices();

        try {
            loadReservationData();
            suggestNextReservationID();
            reservationDateTxt.setText(dateAndTime.addDate());
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load reservation data.", e.getMessage());
        }

        resUpdateBtn.setDisable(true);
        resDeleteBtn.setDisable(true);
    }

    public void changeFocusText(){
        TextField[] textFields = {};

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

    private void initializeTable() {
        resrvationIdCol.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        cusIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        reservationDateCol.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("numberOfGuests"));
        specialRequestsCol.setCellValueFactory(new PropertyValueFactory<>("specialRequests"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableIdCol.setCellValueFactory(new PropertyValueFactory<>("tableID"));
    }

//    private void loadStatusChoices() {
//        reservationStatusChoice.setItems(FXCollections.observableArrayList("Pending", "Confirmed", "Completed", "Cancelled"));
//    }

    @FXML
    void loadAllData(ActionEvent event) {
        try {
            loadReservationData();
            clearFields();
            suggestNextReservationID();
            reservationDateTxt.setText(dateAndTime.addDate());
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data.", e.getMessage());
        }
    }

    @FXML
    void addNewCustomer(ActionEvent event) throws IOException {
        newWindowPopUp("Add New Customer", "/view/CustomerPanel.fxml");
    }

    @FXML
    void searchReservationsOfCustomer(ActionEvent event) throws SQLException, ClassNotFoundException {

        String customerID = cusIdTxt.getText();
        if (customerID.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Customer ID", "Please enter a Customer ID to search.");
            return;
        }

        try {
            // Retrieve all reservations for the customer
            ArrayList<ReservationDto> foundReservations = reservationBO.searchReservationsByCustomerID(customerID);
            if (!foundReservations.isEmpty()) {
                // Populate the TableView with the search results
                ObservableList<ReservationDto> reservationList = FXCollections.observableArrayList(foundReservations);
                reservationTable.setItems(reservationList);  // Set the search result as TableView items
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Reservation Not Found", "No reservation found for customer with ID: " + customerID);
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search reservation.", e.getMessage());
        }
    }


    private void loadReservationData() throws ClassNotFoundException, SQLException {
        ObservableList<ReservationDto> reservationList = FXCollections.observableArrayList(reservationBO.getAllReservationDetails());
        reservationTable.setItems(reservationList);
    }

    public LocalDateTime dateTimeFormat() {
        LocalDateTime assignDateTime = null;

        try {
            if (assignmentDateTxt == null || assignmentDateTxt.getValue() == null) {
                throw new IllegalArgumentException("Date must not be null");
            }
            LocalDate inputDate = assignmentDateTxt.getValue();

            if (assignmentTimeTxt == null || assignmentTimeTxt.getText() == null || assignmentTimeTxt.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Time must not be null or empty");
            }

            LocalTime time = LocalTime.parse(assignmentTimeTxt.getText().trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));

            assignDateTime = LocalDateTime.of(inputDate, time);
        } catch (IllegalArgumentException e) {
            System.err.println("Input validation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing date or time: " + e.getMessage());
        }

        return assignDateTime;
    }



    @FXML
    void saveReservation(ActionEvent event) {
        if (assignmentTimeTxt.getText().trim().isEmpty()){
            assignmentTimeTxt.setText("00:00:00");
        }


        try {
            if (validateInputs()) {
                String tableID = tableIdLabel.getText().trim();
                if (tableID.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Required", "No Table ID", "Please select a Table ID.");
                    return;
                }

                ReservationDto reservation = new ReservationDto(
                        reservationIdTxt.getText().trim(),
                        cusIdTxt.getText().trim(),
                        LocalDate.parse(reservationDateTxt.getText().trim()),
                        Integer.parseInt(capacityTxt.getText().trim()),
                        specialReqTxt.getText().trim(),
                        reservationStatusChoice.getValue(),
                        tableID
                );

                boolean result = reservationBO.save(reservation, tableID, dateTimeFormat());
                if(result){
                    showAlert(Alert.AlertType.INFORMATION, "Reservation Saved");
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Failed to Save Reservation");
                }
                loadReservationData();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save reservation.", e.getMessage());
        }
    }


    @FXML
    void updateReservation(ActionEvent event) {
        if (assignmentTimeTxt.getText().trim().isEmpty()){
            assignmentTimeTxt.setText("00:00:00");
        }
        ReservationDto selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        String specialReq = null;
        if (specialReqTxt.getText() != null && !specialReqTxt.getText().trim().isEmpty()) {
            specialReq = specialReqTxt.getText().trim();
        }

        if (selectedReservation == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Reservation Selected", "Please select a reservation to update.");
            return;
        }

        try {
            if (validateInputs()) {
                String tableID = tableIdLabel.getText().trim();
                if (tableID.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Required", "No Table ID", "Please select a Table ID.");
                    return;
                }


                ReservationDto updatedReservation = new ReservationDto(
                        reservationIdTxt.getText().trim(),
                        cusIdTxt.getText().trim(),
                        LocalDate.parse(reservationDateTxt.getText().trim()),
                        Integer.parseInt(capacityTxt.getText().trim()),
                        specialReq,
                        reservationStatusChoice.getValue(),
                        tableID
                );

                boolean result = reservationBO.update(updatedReservation, tableID, dateTimeFormat());
                if(result){
                    showAlert(Alert.AlertType.INFORMATION, "Reservation Updated");
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Failed to Update Reservation");
                }
                loadReservationData();
                clearFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update reservation.", e.getMessage());
        }
    }


    @FXML
    void deleteReservation(ActionEvent event) {
        ReservationDto selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Reservation Selected", "Please select a reservation to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this reservation?");
        confirmation.setContentText("Reservation ID: " + selectedReservation.getReservationID());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleteResult = reservationBO.delete(selectedReservation.getReservationID());
                if(deleteResult){
                    showAlert(Alert.AlertType.INFORMATION, "Reservation Deleted");
                }else{
                    showAlert(Alert.AlertType.INFORMATION, "Failed to Delete Reservation");
                }

                loadReservationData();
                clearFields();
            } catch (ClassNotFoundException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete reservation.", e.getMessage());
            }
        }
    }



    private void showAlert(Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void searchReservationID(ActionEvent event) {

        String searchId = reservationIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Reservation ID", "Please enter a Reservation ID to search.");
            return;
        }

        try {
            ReservationDto foundReservation = reservationBO.searchById(searchId);
            if (foundReservation != null) {
                populateFields(foundReservation);

            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Reservation Not Found", "No reservation found with ID: " + searchId);
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search reservation.", e.getMessage());
        }
    }


    private void populateFields(ReservationDto reservation) throws SQLException, ClassNotFoundException {
        CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();

        reservationIdTxt.setText(reservation.getReservationID());
        cusIdTxt.setText(reservation.getCustomerID());
        reservationDateTxt.setText(reservation.getReservationDate().toString());
        capacityTxt.setText(String.valueOf(reservation.getNumberOfGuests()));
        specialReqTxt.setText(reservation.getSpecialRequests());
        reservationStatusChoice.setValue(reservation.getStatus());
        tableIdLabel.setText(reservation.getTableID());
        cusNameTxt.setText(customerDAOImpl.searchById(reservation.getCustomerID()).getCusName());
        setDateTimeFields(reservationBO.findDateTime(reservation.getReservationID()));

        resSaveBtn.setDisable(true);
        resUpdateBtn.setDisable(false);
        resDeleteBtn.setDisable(false);
    }

    public void setDateTimeFields(LocalDateTime dateTime) {
        try {
            if (dateTime != null) {
                assignmentDateTxt.setValue(dateTime.toLocalDate());

                assignmentTimeTxt.setText(dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } else {
                assignmentDateTxt.setValue(null);
                assignmentTimeTxt.clear();
            }
        } catch (Exception e) {
            System.err.println("Error setting date and time fields: " + e.getMessage());
        }
    }



    private void clearFields() {
        reservationIdTxt.clear();
        cusIdTxt.clear();
        reservationDateTxt.clear();
        capacityTxt.clear();
        specialReqTxt.clear();
        reservationStatusChoice.setValue(null);
        tableIdLabel.setText("");
        cusNameTxt.clear();
        assignmentTimeTxt.clear();
        assignmentDateTxt.setValue(null);

        resSaveBtn.setDisable(false);
        resUpdateBtn.setDisable(true);
        resDeleteBtn.setDisable(true);
    }


    private boolean validateInputs() {
        return !(reservationIdTxt.getText().trim().isEmpty() ||
                cusIdTxt.getText().trim().isEmpty() ||
                reservationDateTxt.getText().trim().isEmpty() ||
                capacityTxt.getText().trim().isEmpty() ||
                reservationStatusChoice.getValue() == null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void suggestNextReservationID() throws SQLException, ClassNotFoundException {
        reservationIdTxt.setText(reservationBO.suggestNextID());
    }

    public void setTableId(String tableId) {
        if (tableIdLabel != null) {
            tableIdLabel.setText(tableId);
        } else {
            System.out.println("tableIdLabel is null");
        }
    }


    @FXML
    void selectFromTable(MouseEvent event) throws SQLException, ClassNotFoundException {
        ReservationDto selectedItem = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }

    @FXML
    void clearTextFields(ActionEvent event) {
        clearFields();
    }


    @FXML
    void searchCustomerId(ActionEvent event) {

    }

    @FXML
    void searchCustomerName(ActionEvent event) throws SQLException, ClassNotFoundException {
        //CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();
        CustomerBO customerBO = new CustomerBOImpl();

        String searchName = cusNameTxt.getText().trim();
        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Name Provided", "Please enter a Name to search.");
            return;
        }

        try {

            ArrayList<CustomerDto> foundCustomers = customerBO.searchByName(searchName);

            if (!foundCustomers.isEmpty()) {
                for (CustomerDto customer : foundCustomers) {
                    cusIdTxt.setText(customer.getCustomerID());
                    cusNameTxt.setText(customer.getCusName());
                    //unitPriceLabel.setText(String.valueOf(customer.getPrice()));  we can use this to get reservations
                }

                ObservableList<CustomerDto> menuItemList = FXCollections.observableArrayList(foundCustomers);
                // menuItemTable.setItems(menuItemList);
                showAlert(Alert.AlertType.INFORMATION, "Customer Found", "Search Results", foundCustomers.size() + " Customer(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Customers Found", "No customers found with the name: " + searchName);
                cusIdTxt.clear();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search customers.", e.getMessage());
        }

    }


    @FXML
    void validateDate(KeyEvent event) {

    }

    @FXML
    void validateId(KeyEvent event) {

    }

    @FXML
    void validateName(KeyEvent event) {

    }

    @FXML
    void validateNumber(KeyEvent event) {

    }

    @FXML
    void validateText(KeyEvent event) {

    }







}