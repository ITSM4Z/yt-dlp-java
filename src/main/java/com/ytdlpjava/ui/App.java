package com.ytdlpjava.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ytdlpjava/app.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Maz Media Downloader");
        stage.show();
    }
}
