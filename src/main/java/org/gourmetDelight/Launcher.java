package org.gourmetDelight;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application{
    public void start(Stage stage) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/login/LogInPage.fxml"));
        Scene scene = new Scene(load);
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/icons/RMS_Logo.png"))));
        stage.setTitle("Gourmet Delight");

        stage.show();
        stage.setResizable(true);
    }
    public static void main(String[] args) {
        launch(args);
    }

}
