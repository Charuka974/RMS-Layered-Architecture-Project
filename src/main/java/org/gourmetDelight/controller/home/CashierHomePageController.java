package org.gourmetDelight.controller.home;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.gourmetDelight.util.KeepUser;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static org.gourmetDelight.util.Navigations.loadStage;
import static org.gourmetDelight.util.Navigations.loadToParent;

public class CashierHomePageController implements Initializable {

    //--------------------------------------------------------------------------

    @FXML
    private Button backToLogInBTN;

    @FXML
    private AnchorPane cashierAnchorPane;

    @FXML
    private Button customersBTN;

    @FXML
    private Button dashboardBTN;

    @FXML
    private Label homeHeaderLabel;

    @FXML
    private Pane homeHeaderPane;

    @FXML
    private Pane homeMenuPane;

    @FXML
    private Pane mainContentPane;

    @FXML
    private Pane mainContentPane1;

    @FXML
    private Button reservationBtn;

    @FXML
    private Button menuBTN;

    @FXML
    private Button ordersBTN;

    @FXML
    private Label clockLabel;

    @FXML
    private Label calendarLabel;

    @FXML
    private Label currentUser;

    @FXML
    private Button tablesBTN;

    public void initialize(URL location, ResourceBundle resources) {

        setCurrentUser();
        displayTime();
        
        try {
            loadToParent(mainContentPane, "/view/Dashboard.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void setCurrentUser() {
        javafx.application.Platform.runLater(() -> {
            String currentUserID = KeepUser.getInstance().getUserID();
            System.out.println("Currently logged-in to CashierHomePage: " + currentUserID);
            if (currentUserID != null) {
                currentUser.setText(currentUserID);
            } else {
                currentUser.setText("User");
                System.out.println("user not found");
            }
        });
    }


    @FXML
    void dashBtnColour(MouseEvent event) {

    }

    @FXML
    void dashBtnColourBack(MouseEvent event) {

    }


    @FXML
    void backToLogin(ActionEvent event) throws IOException {
        loadStage(cashierAnchorPane, "/view/login/LogInPage.fxml");
    }

    @FXML
    void loadDashboard(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/Dashboard.fxml");
    }

    @FXML
    void loadCustomers(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/CustomerPanel.fxml");
    }

    @FXML
    void loadMenu(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/MenuPanel.fxml");
    }

    @FXML
    void loadOrders(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/orders/OrdersPanel.fxml");
    }

    @FXML
    void loadTables(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/reservations/TablePanel.fxml");
    }


    @FXML
    void loadReservations(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/reservations/ReservationPanel.fxml");
    }

    public void displayTime() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

                clockLabel.setText(time.format(new Date()));
                calendarLabel.setText(date.format(new Date()));
            }
        };

        timer.start();
    }


}
