package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class TZController {

    //Formatter do wyświetlania czasu w formacie GG:MM:SS
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    //Formatter do wyświetlania strefy czasu
    private final DateTimeFormatter zoneFormatter = DateTimeFormatter.ofPattern("O VV");
    private final ZoneId currZoneId = ZoneId.systemDefault();
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
    private Label zoneLabel;
    @FXML
    private Label selectedTimeLabel;
    @FXML
    private Label selectedZoneLabel;
    @FXML
    private ComboBox<String> tzList;
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
        }
        //Pobieranie dostępnych stref czasowych
        List<String> zoneIds = ZoneId.getAvailableZoneIds().stream()
                .filter(id -> id.contains("/") && !id.startsWith("Etc") && !id.startsWith("System"))
                .sorted().toList();
        //Dodawanie stref czasowych do listy rozwijanej
        tzList.getItems().addAll(zoneIds);
        tzList.setValue(currZoneId.toString());
        tzList.setOnAction(this::onTimeZoneSelected);
        //Timeline do aktualizacji znacznika czasu co sekundę
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTimeLabel()),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
    //Obsługa wyboru strefy czasowej z listy rozwijanej
    private void onTimeZoneSelected(ActionEvent event) {
        String selectedTimeZone = tzList.getValue();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tz_choice-view.fxml"));
            Parent selectedTZPage = loader.load();

            SelectedTZController controller = loader.getController();
            controller.setTimeZone(selectedTimeZone);

            Scene selectedTZScene = new Scene(selectedTZPage);
            Stage mainStage = (Stage) tzList.getScene().getWindow();
            mainStage.setScene(selectedTZScene);
        } catch (IOException e) {
            error.errorAlert();
        }
    }
    //Aktualizacja etykiety z bieżącym czasem i strefą czasową
    private void updateTimeLabel() {
        ZonedDateTime now = ZonedDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
        zoneLabel.setText(now.format(zoneFormatter));
    }
}


