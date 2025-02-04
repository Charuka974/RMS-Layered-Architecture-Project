package org.gourmetDelight.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.gourmetDelight.dao.SQLUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static org.gourmetDelight.util.Navigations.newWindowPopUp;

public class DashboardController implements Initializable {


    @FXML
    private BarChart<String, Number> inventoryBarChart;

    @FXML
    private BarChart<String, Number> barchart1; // Revenue by Menu Category

    @FXML
    private LineChart<String, Number> linechart1; // Order Trends Over Time

    @FXML
    private PieChart piechart1; // Most Popular Menu Items

    @FXML
    private PieChart piechart2; // Order Type Distribution

    @FXML
    private AnchorPane dashboardAnchorPane;

    @FXML
    private ScrollPane dashboardScrollPane;

    @FXML
    private Button menuItemsBtn;

    @FXML
    private Button reservationsBtn;

    @FXML
    private AnchorPane scrollAnchorPane;

    public void initialize(URL url, ResourceBundle rb){

        loadMostPopularMenuItemsChart();
        loadRevenueByMenuCategoryChart();
        loadOrderTrendsOverTimeChart();
        loadOrderTypeDistributionChart();
        loadInventoryStockLevelsChart();

    }




    // Populate Most Popular Menu Items Chart
    private void loadMostPopularMenuItemsChart() {
        piechart1.setTitle("Most Popular Menu Items");

        try {
            String query = "SELECT m.Name, SUM(o.Quantity) AS TotalSold " +
                    "FROM OrderItems o " +
                    "JOIN MenuItems m ON o.MenuItemID = m.MenuItemID " +
                    "GROUP BY m.Name";
            ResultSet resultSet = SQLUtil.execute(query);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int totalSold = resultSet.getInt("TotalSold");
                piechart1.getData().add(new PieChart.Data(name, totalSold));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Revenue by Menu Category Chart
    private void loadRevenueByMenuCategoryChart() {
        CategoryAxis xAxis = (CategoryAxis) barchart1.getXAxis();
        xAxis.setLabel("Menu Category");
        NumberAxis yAxis = (NumberAxis) barchart1.getYAxis();
        yAxis.setLabel("Revenue");

        barchart1.setTitle("Revenue by Menu Category");

        try {
            String query = "SELECT m.Category, SUM(o.Quantity * o.Price) AS Revenue " +
                    "FROM OrderItems o " +
                    "JOIN MenuItems m ON o.MenuItemID = m.MenuItemID " +
                    "GROUP BY m.Category";
            ResultSet resultSet = SQLUtil.execute(query);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Revenue");

            while (resultSet.next()) {
                String category = resultSet.getString("Category");
                double revenue = resultSet.getDouble("Revenue");
                series.getData().add(new XYChart.Data<>(category, revenue));
            }

            barchart1.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Order Trends Over Time Chart
    private void loadOrderTrendsOverTimeChart() {
        CategoryAxis xAxis = (CategoryAxis) linechart1.getXAxis();
        xAxis.setLabel("Order Date");
        NumberAxis yAxis = (NumberAxis) linechart1.getYAxis();
        yAxis.setLabel("Number of Orders");

        linechart1.setTitle("Order Trends Over Time");

        try {
            String query = "SELECT OrderDate, COUNT(OrderID) AS TotalOrders " +
                    "FROM Orders " +
                    "GROUP BY OrderDate " +
                    "ORDER BY OrderDate";
            ResultSet resultSet = SQLUtil.execute(query);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Orders Over Time");

            while (resultSet.next()) {
                String date = resultSet.getString("OrderDate");
                int totalOrders = resultSet.getInt("TotalOrders");
                series.getData().add(new XYChart.Data<>(date, totalOrders));
            }

            linechart1.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Order Type Distribution Chart
    private void loadOrderTypeDistributionChart() {
        piechart2.setTitle("Order Type Distribution");

        try {
            String query = "SELECT OrderType, COUNT(OrderID) AS TotalOrders " +
                    "FROM Orders " +
                    "GROUP BY OrderType";
            ResultSet resultSet = SQLUtil.execute(query);

            while (resultSet.next()) {
                String type = resultSet.getString("OrderType");
                int totalOrders = resultSet.getInt("TotalOrders");
                piechart2.getData().add(new PieChart.Data(type, totalOrders));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void loadInventoryStockLevelsChart() {
        CategoryAxis xAxis = (CategoryAxis) inventoryBarChart.getXAxis();
        xAxis.setLabel("Item Name");
        NumberAxis yAxis = (NumberAxis) inventoryBarChart.getYAxis();
        yAxis.setLabel("Quantity");

        inventoryBarChart.setTitle("Inventory Stock Levels");

        try {
            String query = "SELECT Name, Quantity FROM InventoryItems";
            ResultSet resultSet = SQLUtil.execute(query);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Current Stock");

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int quantity = resultSet.getInt("Quantity");
                series.getData().add(new XYChart.Data<>(name, quantity));
            }

            inventoryBarChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }














    @FXML
    void loadMenuItems(ActionEvent event) throws IOException {
        newWindowPopUp("Menu Items", "/view/MenuPanel.fxml");
    }

    @FXML
    void loadReservations(ActionEvent event) throws IOException {
        newWindowPopUp("Reservations", "/view/reservations/ReservationPanel.fxml");
    }

    //comment

}
