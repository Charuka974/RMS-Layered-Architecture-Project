package org.gourmetDelight.controller.orders;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.gourmetDelight.bo.custom.*;
import org.gourmetDelight.bo.custom.impl.*;


import org.gourmetDelight.db.DBConnection;
import org.gourmetDelight.dto.CustomerDto;
import org.gourmetDelight.dto.menuItems.MenuItemDto;
import org.gourmetDelight.dto.reservations.TablesDto;
import org.gourmetDelight.dto.tm.OrdersTM;


import org.gourmetDelight.entity.OrderItems;
import org.gourmetDelight.entity.Orders;
import org.gourmetDelight.entity.Payments;
import org.gourmetDelight.entity.custom.ReservationCustom;

import org.gourmetDelight.util.DateAndTime;
import org.gourmetDelight.util.KeepUser;
import org.gourmetDelight.util.Navigations;
import org.gourmetDelight.util.ValidateUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.gourmetDelight.util.Navigations.*;


public class OrdersController implements Initializable {

    @FXML
    private Label tableNumberLbl;

    @FXML
    private JFXTextField tableIdLbl;

    @FXML
    private Label locationLbl;

    @FXML
    private JFXTextField numberOfPeopleLbl;

    @FXML
    private JFXButton clearTXT;

    @FXML
    private JFXButton cusNameSearchBtn;

    @FXML
    private TableColumn<OrdersTM, String> extraCol;

    @FXML
    private JFXButton itemAddBtn;

    @FXML
    private TableColumn<OrdersTM, String> menuItemCol;

    @FXML
    private TableColumn<OrdersTM, String> menuItemIdCol;

    @FXML
    private JFXTextField menuItemIdTxt;

    @FXML
    private JFXTextField menuItemNameTxt;

    @FXML
    private JFXTextField menuItemQtyTxt;

    @FXML
    private JFXButton menuItemSearchBtn;

    @FXML
    private JFXButton newOrderBtn;

    @FXML
    private JFXButton orderCompleteBtn;

    @FXML
    private JFXTextField orderCusIdTxt;

    @FXML
    private JFXTextField orderCusNameTxt;

    @FXML
    private JFXTextField orderDateTxt;

    @FXML
    private JFXButton orderDeleteBtn;

    @FXML
    private JFXButton orderIdSearchBtn;

    @FXML
    private JFXTextField orderIdTxt;

    @FXML
    private JFXTextField orderPaymentId;

    @FXML
    private ChoiceBox<String> orderStatusChoiceBox;

    @FXML
    private ChoiceBox<String> orderTypechoiceBox;

    @FXML
    private JFXButton orderUpdateBtn;

    @FXML
    private JFXTextField orderUserIdTxt;

    @FXML
    private AnchorPane ordersAnchorPane;

    @FXML
    private TableView<OrdersTM> ordersTable;

    @FXML
    private ChoiceBox<String> paymentMethodChoiceBox;


    @FXML
    private JFXButton placeOrderBtn;

    @FXML
    private TableColumn<OrdersTM, Double> priceCol;

    @FXML
    private TableColumn<OrdersTM, Double> qtyCol;

    @FXML
    private JFXTextField reservationIdTxt;

    @FXML
    private JFXButton reservationSearchBtn;

    @FXML
    private JFXButton itemRemoveBtn;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Label unitPriceLabel;

    @FXML
    private Pane reservationDetailPane;

    @FXML
    private HBox reservationIdHBox;

    @FXML
    private JFXButton addNewCustomerBtn;

    @FXML
    private Pane ordersPane;

    @FXML
    private TableColumn<OrdersTM, Double> unitPriceCol;


    CustomerBO customerBO = new CustomerBOImpl();
    MenuItemBO menuItemDAOImpl = new MenuItemBOImpl();
    DateAndTime dateAndTime = new DateAndTime();

    TableBO tableBO = new TableBOImpl();
    ReservationBO reservationDAOImpl = new ReservationBOImpl();
    MenuItemBOImpl MENU_ITEM_MODEL;
    OrderBO ordersBO = new OrderBOImpl();
    private final ValidateUtil validateUtil = new ValidateUtil();
    private final ObservableList<OrdersTM> ordersTMS = FXCollections.observableArrayList();
    ReservationBO  reservationBO = new ReservationBOImpl();


    public OrdersController() {
        this.MENU_ITEM_MODEL = new MenuItemBOImpl();
    }

    public void initialize(URL url, ResourceBundle rb){
        changeFocusText();
        orderUpdateBtn.setVisible(false);

        setReservationVisible();

        setCellValues();
        try {
            loadNeededData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Load data and initialize the page
        try {
            refreshPage();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }


    @FXML
    void addNewCustomer(ActionEvent event) throws IOException {
        newWindowPopUp("Add New Customer", "/view/CustomerPanel.fxml");

    }


    public void loadNeededData() throws SQLException, ClassNotFoundException {
        orderDateTxt.setText(dateAndTime.addDate());
        String currentUserID = KeepUser.getInstance().getUserID();
        orderUserIdTxt.setText(currentUserID);
        orderIdTxt.setText(ordersBO.suggestNextID());
        orderPaymentId.setText(ordersBO.suggestNextPaymentID());

    }


    private void refreshPage() throws SQLException, ClassNotFoundException {

        orderCusIdTxt.clear();
        orderCusNameTxt.clear();
        reservationIdTxt.clear();
        orderStatusChoiceBox.setValue(null);
        paymentMethodChoiceBox.setValue(null);
        orderTypechoiceBox.setValue(null);

        reservationIdHBox.setVisible(false);
        reservationDetailPane.setVisible(false);
        itemRemoveBtn.setDisable(true);
        ordersTable.refresh();
        loadNeededData();
    }

    private void clearTheTable() {
        ordersTMS.clear();

        ordersTable.refresh();

        totalAmountLabel.setText("0.00");
    }


    void setReservationVisible() {
        orderTypechoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Dine in".equals(newValue)) {
                reservationDetailPane.setVisible(true);
                reservationIdHBox.setVisible(true);
            } else {
                reservationDetailPane.setVisible(false);
                reservationIdHBox.setVisible(false);
            }
        });

    }

    private void setCellValues() {
        // Set up the table columns with property values from CartTM class
        menuItemIdCol.setCellValueFactory(new PropertyValueFactory<>("menuItemID"));
        menuItemCol.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        extraCol.setCellValueFactory(new PropertyValueFactory<>("extra"));

        // Bind the cart items observable list to the TableView
        ordersTable.setItems(ordersTMS);
    }

    void calculateTheFullAmount(){
        int cartQty = 0;
        double fullAmount = 0;
        for (OrdersTM ordersTM : ordersTMS) {

            double newQty = ordersTM.getQuantity() + cartQty;
            double unitPrice = ordersTM.getUnitPrice();
            fullAmount = fullAmount + (unitPrice * newQty);

        }
        totalAmountLabel.setText(String.valueOf(fullAmount));

    }

    @FXML
    void addItemToOrder(ActionEvent event) {
        String selectedItemId = menuItemIdTxt.getText();
        String itemName = menuItemNameTxt.getText();
        String itemQtyText = menuItemQtyTxt.getText();


        // Validate the input fields
        if (selectedItemId.isEmpty() || itemName.isEmpty() || itemQtyText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all the required fields!").show();
            return;
        }


        int cartQty;
        try {
            cartQty = Integer.parseInt(itemQtyText);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity entered. Please enter a valid number!").show();
            return;
        }

        // Check if item is already in the cart and update it if so
        for (OrdersTM ordersTM : ordersTMS) {
            if (ordersTM.getMenuItemID().equals(selectedItemId)) {
                // Update the existing item in the cart
                double newQty = ordersTM.getQuantity() + cartQty;
                double unitPrice = ordersTM.getUnitPrice();
                ordersTM.setQuantity(newQty);
                ordersTM.setPrice(unitPrice * newQty);

                ordersTable.refresh();
                clearInputFields();
                return;
            }
        }

        // If the item is not already in the cart, add it as a new entry
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(unitPriceLabel.getText());

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid unit price entered. Please enter a valid price!").show();
            return;
        }

        // Calculate the total price for the item
        double total = unitPrice * cartQty;

        // Create a new OrdersTM object and add it to the observable list
        OrdersTM newOrderItem = new OrdersTM(
                selectedItemId,
                itemName,
                unitPrice,
                (double) cartQty,
                total,
                "" // Assuming no extra information for now
        );

        ordersTMS.add(newOrderItem);

        // Refresh the table and clear the input fields
        ordersTable.refresh();
        clearInputFields();
        calculateTheFullAmount();

    }

    private void clearInputFields() {
        menuItemNameTxt.clear();
        menuItemIdTxt.clear();
        menuItemQtyTxt.clear();
        unitPriceLabel.setText("Unit Price");

    }


    @FXML
    void selectFromOrdersTable(MouseEvent event) {
        itemRemoveBtn.setDisable(false);
    }

    @FXML
    void clearTextfileds(ActionEvent event) {
        clearInputFields();
        orderCusNameTxt.clear();
        orderCusIdTxt.clear();
        orderPaymentId.clear();
        orderTypechoiceBox.setValue(null);
        orderStatusChoiceBox.setValue(null);
        orderDateTxt.clear();
        orderUserIdTxt.clear();
        paymentMethodChoiceBox.setValue(null);
        reservationIdTxt.clear();
        orderIdTxt.clear();


    }

    @FXML
    void completeTheOrder(ActionEvent event) throws JRException, SQLException, ClassNotFoundException {

        orderStatusChoiceBox.setValue("Completed");

        // Check if Table ID is missing
//        if (tableIdLbl.getText().trim().isEmpty()) {
//            showAlert(Alert.AlertType.WARNING, "Missing Table", "Table ID missing", "Please select a table.");
//            return;
//        }
//
//        // Check if Reservation ID is missing for 'Dine In'
//        if (orderTypechoiceBox.getValue().equals("Dine In") && reservationIdTxt.getText().trim().isEmpty()) {
//            showAlert(Alert.AlertType.WARNING, "Missing Reservation", "Reservation ID missing", "Please provide a reservation ID for Dine In.");
//            return;
//        }


        if (tableIdLbl.getText() != null && !tableIdLbl.getText().isEmpty()) {
            if (orderTypechoiceBox.getValue() != null && orderTypechoiceBox.getValue().equals("Dine in")) {

                if (reservationIdTxt.getText() == null || reservationIdTxt.getText().isEmpty()) {

                    tableBO.updateTableStatus(tableIdLbl.getText(), "Available");
                } else {

                    tableBO.updateTableStatus(tableIdLbl.getText(), "Available");
                    reservationDAOImpl.updateReservationStatus(reservationIdTxt.getText(), "Canceled");
                }
            }
        }


        ordersBO.completeTheOrder(orderIdTxt.getText());





    }


    public void showTheBill(String orderID) throws JRException, SQLException, ClassNotFoundException {
        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/RMS_Bill.jrxml"));
        Connection connection = DBConnection.getInstance().getConnection();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P_OrderID", orderID); // Use the correct parameter key here
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer.viewReport(jasperPrint, false);
    }

    @FXML
    void deleteOrder(ActionEvent event) throws SQLException, ClassNotFoundException {
        String deleteOrderID = orderIdTxt.getText();

        // Check if an order is selected
        if (deleteOrderID == null || deleteOrderID.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Order Selected", "Please select an Order to delete.");
            return;
        }

        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this Order?");
        confirmationAlert.setContentText("Order ID: " + deleteOrderID);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Attempt to delete the order
                boolean isDeleted = ordersBO.deleteOrder(deleteOrderID);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order Deleted", "Order deleted successfully.");
                    refreshPage();
                    clearTheTable();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Failed", "Deletion Failed", "Failed to delete the order.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Deletion Error", e.getMessage());
            }
        }
    }




    @FXML
    void loadNewOrder(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        clearTheTable();


    }


    @FXML
    void removeItemFromOrder(ActionEvent event) {
        OrdersTM selectedOrderItem = ordersTable.getSelectionModel().getSelectedItem();

        if (selectedOrderItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an item to remove.").show();
            return;
        }

        ordersTMS.remove(selectedOrderItem);
        ordersTable.refresh();

        calculateTheFullAmount();

        if (ordersTMS.isEmpty()) {
            itemRemoveBtn.setDisable(true);
        }

        new Alert(Alert.AlertType.INFORMATION, "Item removed successfully from the order.").show();

    }


    @FXML
    void placeOrder(ActionEvent event) throws SQLException, ClassNotFoundException, IOException, JRException {
        if (ordersTable.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add items to cart..!").show();
            return;
        }

        calculateTheFullAmount();

        String orderId = orderIdTxt.getText();
        String orderDateText = orderDateTxt.getText();

        String user = orderUserIdTxt.getText();
        String totalAmount = totalAmountLabel.getText();
        String status = orderStatusChoiceBox.getValue();
        String orderType = orderTypechoiceBox.getValue();
        String paymentId = orderPaymentId.getText();
        String paymentMethod = paymentMethodChoiceBox.getValue();


        // Being a Customer is optional
        String customerId;
        if (orderCusIdTxt.getText().isEmpty()) {
            customerId = null;
        } else {
            customerId = orderCusIdTxt.getText();
        }

        // Having a Reservation is optional
        String reservation;
        if (reservationIdTxt.getText().isEmpty()) {
            reservation = null;
        } else {
            reservation = reservationIdTxt.getText();
        }

        // Validate input
        {
            if (orderId.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Order ID is required!").show();
                return;
            }

            if (user.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "User is required!").show();
                return;
            }

            if (totalAmount.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Total Amount is required!").show();
                return;
            }

            if (status == null) {
                new Alert(Alert.AlertType.ERROR, "Order Status is required!").show();
                return;
            }

            if (orderType == null) {
                new Alert(Alert.AlertType.ERROR, "Order Type is required!").show();
                return;
            }

            if (paymentId.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Payment ID is required!").show();
                return;
            }

            if (paymentMethod == null) {
                new Alert(Alert.AlertType.ERROR, "Payment Type is required!").show();
                return;
            }

        }

        // Parse total amount safely
        double total;
        try {
            total = Double.parseDouble(totalAmount);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid total amount format!").show();
            return;
        }

        // Parse order date safely
        LocalDate date;
        try {
            date = LocalDate.parse(orderDateText);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format! Use YYYY-MM-DD").show();
            return;
        }

        ArrayList<OrderItems> orderItemsDtos = new ArrayList<>();
        ArrayList<Payments> paymentsDtos = new ArrayList<>();

        // Collect order items
        for (OrdersTM ordersTM : ordersTMS) {
            OrderItems orderItemsDto = new OrderItems(
                    orderId,
                    ordersTM.getMenuItemID(),
                    ordersTM.getQuantity(),
                    ordersTM.getPrice()
            );
            orderItemsDtos.add(orderItemsDto);
        }

        Payments paymentsDto = new Payments(
                paymentId,
                paymentMethodChoiceBox.getValue(),
                total,
                date
        );
        paymentsDtos.add(paymentsDto);

        Orders ordersDto = new Orders(
                orderId,
                customerId,
                user,
                date,
                total,
                status,
                orderType,
                reservation,
                paymentId
        );

        boolean isSaved = ordersBO.placeOrder(ordersDto, orderItemsDtos, paymentsDtos);

        if (isSaved) {
            //new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully").show();
            if(orderStatusChoiceBox.getValue().equals("Completed")){

                loadPayment(true);
            }else{
                if (orderType.equals("Dine in") && reservation == null) {
                    tableBO.updateTableStatus(tableIdLbl.getText(), "Occupied");
                }else if(orderType.equals("Dine in")){
                    tableBO.updateTableStatus(tableIdLbl.getText(), "Reserved");
                }
                loadPayment(false);
            }

            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Placing Order Failed").show();
        }

        refreshPage();
        placeOrderBtn.setDisable(false);
        clearTheTable();
    }

    void loadPayment(boolean completed) throws IOException, JRException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/PaymentPanel.fxml"));
        AnchorPane pane = loader.load();

        // Get the controller and pass the Table ID
        PaymentController paymentController = loader.getController();
        String paymentId = orderPaymentId.getText().trim();
        String paymentMethod = paymentMethodChoiceBox.getValue().trim();
        String totalAmount = totalAmountLabel.getText().trim();
        String orderID = orderIdTxt.getText().trim();

        paymentController.setPaymentId(paymentId);
        paymentController.setAmount(totalAmount);
        paymentController.setPaymentMethod(paymentMethod);
        paymentController.setOrderId(orderID);
        paymentController.setIfCompleted(completed);
        Navigations navigation = new Navigations();


        navigation.newWindowPopUpWithData("Payments", loader);
    }


    void performTheOrderUpdate() {
        // Retrieve the updated values from the form
//        String orderId = orderIdTxt.getText();
//        String orderDateText = orderDateTxt.getText();
//        String userId = orderUserIdTxt.getText();
//        String totalAmountText = totalAmountLabel.getText();
//        String status = orderStatusChoiceBox.getValue();
//        String orderType = orderTypechoiceBox.getValue();
//        String paymentId = orderPaymentId.getText();
//        String paymentMethod = paymentMethodChoiceBox.getValue();
//        String customerId = orderCusIdTxt.getText().isEmpty() ? null : orderCusIdTxt.getText();
//        String reservationId = reservationIdTxt.getText().isEmpty() ? null : reservationIdTxt.getText();
//
//
//        // Parse total amount safely
//        double totalAmount;
//        try {
//            totalAmount = Double.parseDouble(totalAmountText);
//        } catch (NumberFormatException e) {
//            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid total amount format!", null);
//            return;
//        }
//
//        // Parse order date safely
//        LocalDate orderDate;
//        try {
//            orderDate = LocalDate.parse(orderDateText);
//        } catch (Exception e) {
//            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format! Use YYYY-MM-DD.", null);
//            return;
//        }
//
//        // Create an updated OrdersDto object
//        OrdersDto updatedOrder = new OrdersDto(
//                orderId,
//                customerId,
//                userId,
//                orderDate,
//                totalAmount,
//                status,
//                orderType,
//                reservationId,
//                paymentId
//        );
//
//        // Create a list of updated order items (newOrderItemsDtos)
//        ArrayList<OrderItemsDto> newOrderItemsDtos = new ArrayList<>();
//        for (OrdersTM item : ordersTable.getItems()) {
//            String menuItemID = item.getMenuItemID();
//            double quantity = item.getQuantity();
//            double price = item.getPrice();
//
//            // Add to the list
//            newOrderItemsDtos.add(new OrderItemsDto(orderId, menuItemID, quantity, price));
//        }
//
//        // Create a list of updated payments (paymentsDtos)
//        ArrayList<PaymentsDto> paymentsDtos = new ArrayList<>();
//        PaymentsDto paymentDto = new PaymentsDto(paymentId, paymentMethod, totalAmount, LocalDate.now());
//        paymentsDtos.add(paymentDto);
//
//        // Attempt to update the order in the database
//        try {
//            boolean isUpdated = ordersModel.updateOrder(updatedOrder, newOrderItemsDtos, paymentsDtos);
//
//            if (isUpdated) {
//                showAlert(Alert.AlertType.INFORMATION, "Success", "Order Updated", "The order was successfully updated.");
//                refreshPage(); // Reload the form with updated data
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Update Failed", "The order update failed.", null);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update the order.", e.getMessage());
//        }
    }



    @FXML
    void searchCustomerName(ActionEvent event) {
        String searchName = orderCusNameTxt.getText().trim();
        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Name Provided", "Please enter a Name to search.");
            return;
        }

        try {

            ArrayList<CustomerDto> foundCustomers = customerBO.searchByName(searchName);

            if (!foundCustomers.isEmpty()) {
                for (CustomerDto customer : foundCustomers) {
                    orderCusIdTxt.setText(customer.getCustomerID());
                    orderCusNameTxt.setText(customer.getCusName());
                    //unitPriceLabel.setText(String.valueOf(customer.getPrice()));  we can use this to get reservations
                }

                ObservableList<CustomerDto> menuItemList = FXCollections.observableArrayList(foundCustomers);
                // menuItemTable.setItems(menuItemList);
                showAlert(Alert.AlertType.INFORMATION, "Customer Found", "Search Results", foundCustomers.size() + " Customer(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Customers Found", "No customers found with the name: " + searchName);
                orderCusIdTxt.clear();
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search customers.", e.getMessage());
        }


    }

    @FXML
    void searchForMenuItemId(ActionEvent event) {
        // we need to get the price too
        // we can also get the full name of the menu item
        String searchName = menuItemNameTxt.getText().trim();

        if (searchName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "No Name Provided", "Please enter a Menu Item Name to search.");
            return;
        }

        try {
            ArrayList<MenuItemDto> foundMenuItems = MENU_ITEM_MODEL.searchByName(searchName);

            if (!foundMenuItems.isEmpty()) {

                for (MenuItemDto menuItem : foundMenuItems) {
                    menuItemIdTxt.setText(menuItem.getMenuItemID());
                    unitPriceLabel.setText(String.valueOf(menuItem.getPrice()));
                    menuItemNameTxt.setText(menuItem.getName());
                }

                ObservableList<MenuItemDto> menuItemList = FXCollections.observableArrayList(foundMenuItems);
                // menuItemTable.setItems(menuItemList);
                showAlert(Alert.AlertType.INFORMATION, "Menu Items Found", "Search Results", foundMenuItems.size() + " menu item(s) found with the name: " + searchName);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No Menu Items Found", "No menu items found with the name: " + searchName);
                menuItemIdTxt.setText(null);
                unitPriceLabel.setText(null);
            }
        } catch (ClassNotFoundException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search menu items.", e.getMessage());
        }


    }

    @FXML
    void searchOrderId(ActionEvent event) throws SQLException, ClassNotFoundException {

        try {
            String orderId = orderIdTxt.getText();
            showTheBill(orderIdTxt.getText());
            String paymentId = null;

            // Retrieve order details based on orderId
            Orders orderDetails = ordersBO.searchById(orderId);
            if (orderDetails != null) {
                // Set the order details into the respective text fields
                orderCusIdTxt.setText(orderDetails.getCustomerID());
                orderUserIdTxt.setText(orderDetails.getUserID()); // Assuming UserID represents customer name, adjust if necessary
                orderDateTxt.setText(orderDetails.getOrderDate().toString());
                orderStatusChoiceBox.setValue(orderDetails.getStatus());
                paymentId = orderDetails.getPaymentID();
                orderPaymentId.setText(paymentId);
                orderTypechoiceBox.setValue(orderDetails.getOrderType());
                reservationIdTxt.setText(orderDetails.getReservationID());

                if(orderDetails.getCustomerID() == null){
                    orderCusNameTxt.clear();
                }else{
                    orderCusNameTxt.setText(customerBO.searchById(orderDetails.getCustomerID()).getCusName());
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Order not found!").show();
            }

            // Retrieve order items related to this orderId
            ArrayList<OrderItems> orderItems = ordersBO.getOrderItemsByOrderId(orderId);
            if (orderItems != null) {
                ordersTMS.clear();
                for (OrderItems item : orderItems) {
                    OrdersTM orderTM = new OrdersTM(
                            item.getMenuItemID(),
                            getMenuItemName(item.getMenuItemID()),
                            getMenuItemPrice(item.getMenuItemID()),
                            item.getQuantity(),
                            item.getPrice(),
                            null
                    ); // extraInfo is not available in DTO, set empty
                    ordersTMS.add(orderTM);
                }
                ordersTable.setItems(ordersTMS);
            }


            // Retrieve payment details for the order
            Payments paymentDetails = ordersBO.getPaymentById(paymentId);
            if (paymentDetails != null) {
                orderPaymentId.setText(paymentDetails.getPaymentID());
                totalAmountLabel.setText(String.valueOf(paymentDetails.getAmount())); // Assuming amount is the total amount
                paymentMethodChoiceBox.setValue(paymentDetails.getPaymentMethod());
            } else {
                new Alert(Alert.AlertType.ERROR, "Payment details not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error retrieving order data: " + e.getMessage()).show();
        } catch (JRException e) {
            throw new RuntimeException(e);
        }

        loadTableData();
    }


    public String getMenuItemName(String menuItemId) {
        String name = null;
        try {
            MenuItemDto menuItem = menuItemDAOImpl.searchById(menuItemId);

            if (menuItem != null) {
                name = menuItem.getName();
            } else {

                System.out.println("Menu item with ID " + menuItemId + " not found.");
            }
        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Error: " + e.getMessage());
        }
        return name;
    }


    public double getMenuItemPrice(String menuItemId) {

        double price = 0.0;
        try {
            MenuItemDto menuItem = menuItemDAOImpl.searchById(menuItemId);

            if (menuItem != null) {
                price = menuItem.getPrice();
            } else {
                System.out.println("Menu item with ID " + menuItemId + " not found.");
            }
        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Error: " + e.getMessage());
        }
        return price;
    }



    @FXML
    void searchReservationId(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadTableData();
    }


    static int availableSearchTimes = 0;
    void loadTableData() throws SQLException, ClassNotFoundException {
        String reservationId = reservationIdTxt.getText();  // Get the reservation ID from the text field


        if (reservationId == null || reservationId.trim().isEmpty()) {

            int numberOfPeople = 0;

            try {

                numberOfPeople = Integer.parseInt(numberOfPeopleLbl.getText());
                if (numberOfPeople < 0) {
                    throw new IllegalArgumentException("Number of people cannot be negative.");
                }
            } catch (IllegalArgumentException e) {
                numberOfPeopleLbl.setText("0");
                return;
            }

            ArrayList<TablesDto> tables = tableBO.findAvailableTable(numberOfPeople);
            if (tables.isEmpty()) {
                tableIdLbl.setText("N/A");  // N/A   means Not Available
                locationLbl.setText("N/A");
                tableNumberLbl.setText("N/A");
                return;
            }


            if (availableSearchTimes >= tables.size()) {
                availableSearchTimes = 0;
            }

            TablesDto table = tables.get(availableSearchTimes);
            tableIdLbl.setText(table.getTableID());
            locationLbl.setText(table.getLocation());
            tableNumberLbl.setText(table.getTableNumber());

            availableSearchTimes++;
        } else {

            try {

                ReservationCustom orderReservation = reservationBO.getJoinReservationDetails(reservationId);
                if (orderReservation == null) {
                    System.out.println("Reservation not found for ID: " + reservationId);
                    return;
                }


                numberOfPeopleLbl.setText(String.valueOf(orderReservation.getNumberOfGuests()));


                TablesDto tableDetail = tableBO.searchById(orderReservation.getTableID());
                if (tableDetail != null) {

                    tableIdLbl.setText(orderReservation.getTableID());
                    locationLbl.setText(tableDetail.getLocation());
                    tableNumberLbl.setText(tableDetail.getTableNumber());
                } else {

                    System.out.println("Table not found for Reservation ID: " + reservationId);
                    tableIdLbl.setText("N/A");
                    locationLbl.setText("N/A");
                    tableNumberLbl.setText("N/A");
                }
            } catch (Exception e) {

                System.err.println("Error fetching reservation details: " + e.getMessage());
            }
        }
    }



    @FXML
    void updateOrder(ActionEvent event) {
        performTheOrderUpdate();
    }

    public void changeFocusText(){
        TextField[] textFields = {orderIdTxt, orderCusNameTxt, orderCusIdTxt, menuItemNameTxt, menuItemQtyTxt };

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




}
