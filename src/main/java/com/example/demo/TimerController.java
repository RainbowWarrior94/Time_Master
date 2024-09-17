package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TimerController {

    private Timer timer; //Timer do odliczania czasu
    private boolean isTimerRunning = false; //Stan timera
    private Clip timerSound; //Dźwięk odtwarzany po zakończeniu timera
    ErrorAlert error = new ErrorAlert(); //Obiekt do wyświetlania błędów

    @FXML
    private Button AlarmButton;
    @FXML
    private Button TZButton;
    @FXML
    private Button TimerButton;
    @FXML
    private Button stopTimerButton;
    @FXML
    private ComboBox<Integer> hoursInput;
    @FXML
    private ComboBox<Integer> minutesInput;
    @FXML
    private ComboBox<Integer> secondsInput;
    @FXML
    private Button setTimerButton;
    @FXML
    private Label timeLeftLabel;
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
    //Metoda ustawiająca timer
    @FXML
    void setTimer(ActionEvent event) {

        if (isTimerRunning) {
            return;
        }
        Integer hour = hoursInput.getValue();
        Integer minute = minutesInput.getValue();
        Integer second = secondsInput.getValue();

        if (hour == null) hour = 0;
        if (minute == null) minute = 0;
        if (second == null) second = 0;

        if (hour == 0 && minute == 0 && second == 0) {
            error.errorAlert();
            return;
        }

        int totalSeconds = hour * 3600 + minute * 60 + second;

        isTimerRunning = true;

        timer = new Timer();
        TimerTask task = new TimerTask() {
            int remainingSeconds = totalSeconds;

            @Override
            public void run() {
                if (remainingSeconds > 0) {
                    int hours = remainingSeconds / 3600;
                    int minutes = (remainingSeconds % 3600) / 60;
                    int seconds = remainingSeconds % 60;
                    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    Platform.runLater(() -> timeLeftLabel.setText(timeString));
                    remainingSeconds--;
                } else {
                    Platform.runLater(() -> timeLeftLabel.setText("Time's up!"));
                    if (timerSound != null) {
                        timerSound.setFramePosition(0);
                        timerSound.start();
                    }
                    timer.cancel();
                    isTimerRunning = false;
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    // Metoda zatrzymująca timer
    @FXML
    void stopTimer() {
        if (timer != null) {
            timer.cancel();
            isTimerRunning = false;
        }
    }
    // Metoda obsługująca przycisk zatrzymania timera
    @FXML
    void stopTimerButton(ActionEvent event) {
        stopTimer();
    }

    public void initialize() {
        Font clockFont = Font.loadFont(getClass().getResourceAsStream("/digital-7__mono_.ttf"), 40);
        if (clockFont != null) {
            timeLeftLabel.setFont(clockFont);
        }

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

        List<Integer> secondsList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            secondsList.add(i);
        }
        secondsInput.getItems().addAll(secondsList);
        secondsInput.setCellFactory(listView -> new FormatListCell());
        secondsInput.setButtonCell(new FormatListCell());

        initializeSound();
    }
    //Metoda inicjalizująca dźwięk timera
    private void initializeSound() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/timer.wav");
            if (audioSrc == null) {
                throw new IOException("Audio resource not found");
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            timerSound = AudioSystem.getClip();
            timerSound.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


