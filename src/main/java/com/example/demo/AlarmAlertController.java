package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AlarmAlertController {

    @FXML
    private Label alertMessageLabel;

    private Stage stage;
    //Metoda do ustawiania bieżącego okna
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    //Obsługa zdarzenia dla przycisku "OK"
    @FXML
    private void handleOkButton() {
        stage.close(); //Zamknięcie okna
    }

    public void initialize() {
        //Ustawianie czcionki
        Font clockFont = Font.loadFont(getClass().getResourceAsStream("/digital-7__mono_.ttf"), 40);
        if (clockFont != null) {
            alertMessageLabel.setFont(clockFont);
        }
    }
}
