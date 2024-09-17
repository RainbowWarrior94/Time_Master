package com.example.demo;

import javafx.scene.control.Alert;

public class ErrorAlert {
    public void errorAlert() {
        Alert errorMessage = new Alert(Alert.AlertType.WARNING);
        errorMessage.setTitle("Error window");
        errorMessage.setHeaderText("Alert!");
        errorMessage.setContentText("Something went wrong...");
        errorMessage.showAndWait();
    }
}

