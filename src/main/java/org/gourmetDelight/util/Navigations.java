package org.gourmetDelight.util;

import com.sun.tools.javac.Main;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Navigations {
    private static Stage stage;
    private static Scene scene;

    public static void loadPanel(Pane pane, String link) throws IOException {
        pane.getChildren().clear();
        pane.getChildren().add(FXMLLoader.load(Navigations.class.getResource(link)));
    }

    public static void loadStage(Pane pane, String link) throws IOException {
        stage = (Stage) pane.getScene().getWindow();
        Parent load = FXMLLoader.load(Navigations.class.getResource(link));
        scene = new Scene(load);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void loadToParent(Pane pane, String link) throws IOException {
        Parent root = FXMLLoader.load(Navigations.class.getResource(link));
        pane.getChildren().clear();
        pane.getChildren().add(root);
    }

//    public static void loadToParent(Pane pane, String link) throws IOException {
//        Parent root = FXMLLoader.load(Navigations.class.getResource(link));
//        pane.getChildren().clear();
//        pane.getChildren().add(root);
//    }

    public void loadPage(Pane pane, String link) throws IOException {
        pane.getChildren().add(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource(link))));
    }

    public static void newWindowPopUp(String title, String path) throws IOException {
        Parent load = FXMLLoader.load(Navigations.class.getResource(path));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
    //  stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.jpg")));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);

        // Show and wait for the window to be closed before returning to the main program
        stage.showAndWait();
    //  stage.show();
    }

    public void newWindowPopUpWithData(String title, FXMLLoader loader) {
        try {
            Parent root = loader.getRoot(); // Use the existing loader instance
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void onTheTopNavigation(Pane pane, String link) {
//        try {
//            FXMLLoader loader = new FXMLLoader(Navigations.class.getResource("/view/" + link));
//            Parent root = loader.load();
//            pane.getChildren().add(root);
//            FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), pane);
//            fadeIn.setFromValue(0.0);
//            fadeIn.setToValue(1.0);
//            fadeIn.play();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void switchNavigation(String link, ActionEvent event) {
//        try {
//            Parent root = FXMLLoader.load(Navigations.class.getResource("/view/" + link));
//            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            scene = new Scene(root);
//            stage.setScene(scene);
//            stage.centerOnScreen();
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void switchNavigation(String link, MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(Navigations.class.getResource("/view/" + link));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.centerOnScreen();
//        close(event);
//        stage.show();
//    }
//
//    public static void popupNavigation(String link,String title){
//        try {
//            URL resource = Navigations.class.getResource("/view/" + link);
//            Parent parent = FXMLLoader.load(resource);
//            Scene scene = new Scene(parent);
//            Stage stage = new Stage();
//            stage.setAlwaysOnTop(true);
//            stage.setTitle(title);
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void close(ActionEvent actionEvent) {
//        Node node = (Node) actionEvent.getSource();
//        stage = (Stage) node.getScene().getWindow();
//        stage.close();
//    }
//
//    public static void close(MouseEvent actionEvent) {
//        Node node = (Node) actionEvent.getSource();
//        stage = (Stage) node.getScene().getWindow();
//        stage.close();
//    }


    public static void close(Pane pane) {
        pane.getChildren().clear();
    }

    public static void setInvisible(Pane pane) {
        pane.setVisible(false);
    }

}
