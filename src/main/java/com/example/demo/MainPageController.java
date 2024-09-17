package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MainPageController {

    public Text nameText;
    //Formatter do wyświetlania czasu w formacie GG:MM:SS
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    ErrorAlert error = new ErrorAlert(); //Obiekt do wyświetlania błędów

    @FXML
    private Button AlarmButton;
    @FXML
    private Button TZButton;
    @FXML
    private Button TimerButton;
    @FXML
    private Label timeLabel;
    @FXML
    public void initialize() {

        //Ustawianie czcionki do wyświetlania czasu
        Font clockFont = Font.loadFont(getClass().getResourceAsStream("/digital-7__mono_.ttf"), 75);
        if (clockFont != null) {
            timeLabel.setFont(clockFont);
        }
        //Ustawianie czcionki do wyświetlania nazwy
        Font nameFont = Font.loadFont(getClass().getResourceAsStream("/digital-7__mono_.ttf"), 55);
        if (nameFont != null) {
            nameText.setFont(nameFont);
        }
        //Timeline do aktualizacji znacznika czasu co sekundę
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTimeLabel()),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
    @FXML
    void onAlarmButtonClick(ActionEvent event) {
        try {
            // Przejście do ekranu alarmu (alarm-view.xml)
            Parent AlarmPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("alarm-view.fxml")));
            Scene AlarmScene = new Scene(AlarmPage);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(AlarmScene);
        } catch (IOException e) {
            error.errorAlert();
        }
    }
    @FXML
    void onTZButtonClick(ActionEvent event) throws IOException {
        try {
            //Przejście do ekranu stref czasowych (tz-view.xml)
            Parent TZPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("tz-view.fxml")));
            Scene TZScene = new Scene(TZPage);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(TZScene);
        } catch (IOException e) {
            error.errorAlert();
        }
    }
    @FXML
    void onTimerButtonClick(ActionEvent event) {
        try {
            //Przejście do ekranu timera (timer-view.xml)
            Parent TimerPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("timer-view.fxml")));
            Scene TimerScene = new Scene(TimerPage);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(TimerScene);
        } catch (IOException e) {
            error.errorAlert();
        }
    }
    //Metoda aktualizacji znacznika czasu
    private void updateTimeLabel() {
        timeLabel.setText(LocalDateTime.now().format(timeFormatter));
    }
}

