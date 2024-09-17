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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class SelectedTZController {

    //Formatter do wyświetlania czasu w formacie GG:MM:SS
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    //Formatter do wyświetlania strefy czasu
    private final DateTimeFormatter zoneFormatter = DateTimeFormatter.ofPattern("O VV");
    private String selectedTimeZone = "UTC"; //Domyślna strefa czasowa
    ErrorAlert error = new ErrorAlert(); //Obiekt do wyświetlania błędów

    @FXML
    private Button AlarmButton;
    @FXML
    private Button TZButton;
    @FXML
    private Button TimerButton;
    @FXML
    private Label selectedTimeLabel;
    @FXML
    private Label selectedZoneLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label zoneLabel;
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
    public void initialize() {
        //Ustawienie czcionki dla wyświetlania czasu
        Font clockFont = Font.loadFont(getClass().getResourceAsStream("/digital-7__mono_.ttf"), 40);
        if (clockFont != null) {
            timeLabel.setFont(clockFont);
            selectedTimeLabel.setFont(clockFont);
        }
        //Timeline do aktualizacji znacznika czasu co sekundę
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTimeLabels()),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
    private void updateTimeLabels() {
        //Aktualizacja etykiet czasu dla bieżącej strefy czasowej
        ZonedDateTime now = ZonedDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
        zoneLabel.setText(now.format(zoneFormatter));

        //Aktualizacja etykiet czasu dla wybranej strefy czasowej
        ZonedDateTime selectedNow = ZonedDateTime.now(ZoneId.of(selectedTimeZone));
        selectedTimeLabel.setText(selectedNow.format(timeFormatter));
        selectedZoneLabel.setText(selectedNow.format(zoneFormatter));
    }
    public void setTimeZone(String timeZone) {
        this.selectedTimeZone = timeZone; //Ustawienie wybranej strefy czasowej i aktualizacja etykiet
        updateTimeLabels();
    }
}
