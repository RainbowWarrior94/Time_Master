package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlarmController {

    //Formatter do wyświetlania czasu w formacie GG:MM:SS
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final List<LocalDateTime> alarms = new ArrayList<>(); //Lista alarmów
    private Clip alarmSound; //Dźwięk alarmu
    ErrorAlert error = new ErrorAlert(); //Obiekt do wyświetlania błędów

    @FXML
    private Button AlarmButton;
    @FXML
    private Button TZButton;
    @FXML
    private Button TimerButton;
    @FXML
    private Button setAlarmButton;
    @FXML
    private Button showAlarmsButton;
    @FXML
    private DatePicker dateInput;
    @FXML
    private ComboBox<Integer> hoursInput;
    @FXML
    private ComboBox<Integer> minutesInput;
    @FXML
    private Label timeLabel;
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
    @FXML
    void setAlarm(ActionEvent event) {
        LocalDate date = dateInput.getValue();
        Integer hour = hoursInput.getValue();
        Integer minute = minutesInput.getValue();

        if (date != null && hour != null && minute != null) {
            LocalDateTime alarmDateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));
            LocalDateTime now = LocalDateTime.now();

            if (alarmDateTime.isAfter(now)) {
                alarms.add(alarmDateTime); //Dodanie nowego alarmu do listy
            } else {
                //Okno błędu
                Alert errorMessage = new Alert(Alert.AlertType.WARNING);
                errorMessage.setTitle("Error window");
                errorMessage.setHeaderText("Alert!");
                errorMessage.setContentText("You cannot set an alarm for past time.");
                errorMessage.showAndWait();
            }
        } else {
            error.errorAlert();
        }
    }
    @FXML
    void showAlarms(ActionEvent event) {
        try {
            //Ekran z listą alarmów
            FXMLLoader loader = new FXMLLoader(getClass().getResource("show-alarms.fxml"));
            Parent root = loader.load();

            Stage showAlarmsStage = new Stage();
            showAlarmsStage.setTitle("All Alarms");
            showAlarmsStage.setScene(new Scene(root));

            ShowAlarmsController controller = loader.getController();
            controller.setStage(showAlarmsStage);
            controller.setAlarmsList(alarms);
            showAlarmsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        //Ustawienie czcionki dla wyświetlania czasu
        Font clockFont = Font.loadFont(getClass().getResourceAsStream("/digital-7__mono_.ttf"), 40);
        if (clockFont != null) {
            timeLabel.setFont(clockFont);
        }
        initializeSound(); //Inicjalizacja dźwięku alarmu
        //Timeline do aktualizacji znacznika czasu co sekundę
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTimeLabel()),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        //Wypełnienie listy godzin i minut
        List<Integer> hoursList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(i);
        }
        hoursInput.getItems().addAll(hoursList);
        hoursInput.setCellFactory(listView -> new FormatListCell());
        hoursInput.setButtonCell(new FormatListCell());

        List<Integer> minutesList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutesList.add(i);
        }
        minutesInput.getItems().addAll(minutesList);
        minutesInput.setCellFactory(listView -> new FormatListCell());
        minutesInput.setButtonCell(new FormatListCell());

        startAlarmCheckTimer(); //Uruchomienie zegara sprawdzającego alarmy
    }

    private void updateTimeLabel() {
        timeLabel.setText(LocalDateTime.now().format(timeFormatter)); //Aktualizacja etykiety czasu
    }

    private void startAlarmCheckTimer() {
        //Timeline do aktualizacji znacznika czasu co sekundę
        Timeline alarmCheckTimer = new Timeline(new KeyFrame(Duration.ZERO, e -> checkAlarms()),
                new KeyFrame(Duration.seconds(1))
        );
        alarmCheckTimer.setCycleCount(Timeline.INDEFINITE);
        alarmCheckTimer.play();
    }

    private boolean isAlarmTime(LocalDateTime alarm) {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() == alarm.getHour() && now.getMinute() == alarm.getMinute(); //Sprawdzenie, czy nadszedł czas alarmu
    }

    private void checkAlarms() {
        List<LocalDateTime> triggeredAlarms = new ArrayList<>();

        for (LocalDateTime alarm : alarms) {
            if (isAlarmTime(alarm)) {
                triggeredAlarms.add(alarm); //Dodanie wyzwolonego alarmu do listy
                Platform.runLater(() -> {
                    try {
                        //Okno powiadomienia o alarmie
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("alarm-window.fxml"));
                        Parent root = loader.load();
                        Stage alertStage = getStage(root, loader);
                        alertStage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                playAlarmSound(); //Odtworzenie dźwięku alarmu
            }
        }
        alarms.removeAll(triggeredAlarms); //Usunięcie wyzwolonych alarmów z listy
    }

    private Stage getStage(Parent root, FXMLLoader loader) {
        Stage alertStage = new Stage();
        alertStage.setTitle("Alarm Alert");
        alertStage.setScene(new Scene(root));

        AlarmAlertController controller = loader.getController();
        controller.setStage(alertStage);

        alertStage.setOnHidden(event -> { stopAlarmSound(); }); //Zatrzymanie dźwięku alarmu po zamknięciu okna
        return alertStage;
    }

    private void initializeSound() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/alarm.wav");
            if (audioSrc == null) {
                throw new IOException("Audio resource not found");
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            alarmSound = AudioSystem.getClip();
            alarmSound.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAlarmSound() {
        if (alarmSound != null) {
            alarmSound.setFramePosition(0);
            alarmSound.loop(Clip.LOOP_CONTINUOUSLY); //Odtwarzanie dźwięku w pętli
        }
    }

    private void stopAlarmSound() {
        if (alarmSound != null && alarmSound.isRunning()) {
            alarmSound.stop(); //Zatrzymanie dźwięku
            alarmSound.setFramePosition(0);
        }
    }
}

