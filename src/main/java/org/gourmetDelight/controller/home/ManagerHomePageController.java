package org.gourmetDelight.controller.home;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.gourmetDelight.model.home.HomeModel;
import org.gourmetDelight.util.KeepUser;


import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;



import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static org.gourmetDelight.util.Navigations.loadStage;
import static org.gourmetDelight.util.Navigations.loadToParent;


public class ManagerHomePageController implements Initializable {

    @FXML
    private AnchorPane managerAnchorPaneHome;

    @FXML
    private Button backToLogInBTN;

    @FXML
    private Button dashboardBTN;

    @FXML
    private Button employeeButton;

    @FXML
    private Label clockLabel;

    @FXML
    private Label calendarLabel;

    @FXML
    private Label homeHeaderLabel;

    @FXML
    private Pane homeHeaderPane;

    @FXML
    private Pane homeMenuPane;

    @FXML
    private Pane mainContentPane;

    @FXML
    private Button menuBTN;

    @FXML
    private Button ordersBTN;

    @FXML
    private Button stockPurchaseBtn;

    @FXML
    private Button inventoryBtn;

    @FXML
    private Button customersBtn;

    @FXML
    private Button reservationBtn;

    @FXML
    private Button tablesBTN;

    @FXML
    private Label currentUser;

    private Timer timer;


    public void initialize(URL location, ResourceBundle resources) {
        // when i close the program i want to stop the timing process
        Platform.runLater(() -> {
            Stage stage = (Stage) managerAnchorPaneHome.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                stopTimer();  // Stop the timer when the window is closed
            });
        });

        setCurrentUser();
        displayTime();

        try {
            loadToParent(mainContentPane, "/view/Dashboard.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        startReminderCheck();


    }



    private void startReminderCheck() {
        // Check the time (every minute) while running to check if there are reminders
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Run the reminder check after every 1 minute
                Platform.runLater(() -> {
                    HomeModel homeModel = new HomeModel();
                    homeModel.checkAndPrintReminder();
                });
            }
        }, 0, 60000);  // 0 initial delay, 60000ms (1 minute) interval
    }



    void setCurrentUser() {
        javafx.application.Platform.runLater(() -> {
             /*
         Using Platform.runLater() ensures the UI label currentUser is updated on the JavaFX Application Thread,
         preventing concurrency issues.
        It ensures any UI update code, like setting currentUser’s text,
         runs after JavaFX completes any remaining initialization work,
          so the latest userID is retrieved and displayed properly.
        */
            String currentUserID = KeepUser.getInstance().getUserID();
            System.out.println("Currently logged-in to ManagerHomePage: " + currentUserID);
            if (currentUserID != null) {
                currentUser.setText(currentUserID);
            } else {
                currentUser.setText("User");
                System.out.println("user not found");
            }
        });
    }

    @FXML
    void backToLogin(ActionEvent event) throws IOException {
        loadStage(managerAnchorPaneHome, "/view/login/LogInPage.fxml");

        stopTimer();
    }

    @FXML
    void loadDashboard(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/Dashboard.fxml");
    }

    @FXML
    void loadEmployee(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/employee/EmployeePanel.fxml");
    }

    @FXML
    void loadCustomerPanel(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/CustomerPanel.fxml");
    }

    @FXML
    void loadInventory(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/inventory/InventoryPanel.fxml");
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

    @FXML
    void loadStockPurchases(ActionEvent event) throws IOException {
        loadToParent(mainContentPane, "/view/inventory/StockPurchase.fxml");
    }


    public void displayTime() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat time =  new SimpleDateFormat("HH:mm:ss");

                clockLabel.setText(time.format(new Date()));
                calendarLabel.setText(date.format(new Date()));
            }
        };

        timer.start();
    }



    private void stopTimer() {
        if (timer != null) {
            timer.cancel();  // This will stop the timer
            timer.purge();    // clear data, cache
            System.out.println("Timer stopped.");
        }
    }


}