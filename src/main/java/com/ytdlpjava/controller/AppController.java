package com.ytdlpjava.controller;

import com.ytdlpjava.core.ProgressListener;
import com.ytdlpjava.core.YoutubeDLExecutor;
import com.ytdlpjava.core.YoutubeDLRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class AppController {

    @FXML private TextField urlField;
    @FXML private TextField pathField;
    @FXML private ProgressBar progressBar;
    @FXML private Button downloadBtn;
    @FXML private Label percentageLabel;

    private final YoutubeDLExecutor executor = new YoutubeDLExecutor();

    @FXML
    public void handleDownload(){
        var url = urlField.getText();
        if (url == null || url.isEmpty()) return;

        var downloadPath = pathField.getText();
        if(downloadPath.isEmpty())
            downloadPath = System.getProperty("user.home") + File.separator + "Downloads";

        YoutubeDLRequest request = YoutubeDLRequest.builder(url, downloadPath).build();

        urlField.setDisable(true);

        executor.execute(request, new ProgressListener() {
            @Override
            public void onProgress(float percentage, String speed, String eta) {
                Platform.runLater(() -> {
                    String text = String.format("%.1f%% (%s)", percentage, speed);
                    percentageLabel.setText(text);
                    progressBar.setProgress(percentage / 100.0);
                });
            }

            @Override
            public void onStart() {
                Platform.runLater(() -> percentageLabel.setText("Starting..."));
            }

            @Override
            public void onComplete() {
                Platform.runLater(() -> {
                    percentageLabel.setText("Done! Downloaded at: ");
                    resetUI();
                });
            }

            @Override
            public void onError(String error) {
                Platform.runLater(() -> {
                    percentageLabel.setText("Error: " + error);
                    resetUI();
                });
            }
        });
    }

    @FXML
    public void handleBrowse(){
        var directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Download Destination");

        File selectedDirectory = directoryChooser.showDialog(urlField.getScene().getWindow());

        if(selectedDirectory != null)
            pathField.setText(selectedDirectory.getAbsolutePath());
    }

    private void resetUI(){
        downloadBtn.setDisable(false);
        urlField.setDisable(false);
        progressBar.setProgress(0);
    }
}
