package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ShowAlarmsController {

    private Stage stage;
    private List<LocalDateTime> alarms;

    @FXML
    private ListView<String> alarmsListView; //Lista wyświetlająca alarmy
    @FXML
    private void handleCloseButton() {
        stage.close(); //Zamknięcie okna dialogowego
    }

    public void setStage(Stage stage) {
        this.stage = stage; //Ustawienie sceny
    }
    public void setAlarmsList(List<LocalDateTime> alarms) {
        this.alarms = alarms;
        updateAlarmsListView();
    }

    private void updateAlarmsListView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<String> alarmStrings = alarms.stream()
                .map(alarm -> alarm.format(formatter))
                .collect(Collectors.toList());
        alarmsListView.getItems().setAll(alarmStrings);
    }

    public void initialize() {
        alarmsListView.setCellFactory(param -> new ListCell<>() {
            private final Button deleteButton = new Button("✖");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    //Działanie przycisku usuwania
                    deleteButton.setOnAction(event -> {
                        alarms.removeIf(alarm -> alarm.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).equals(item));
                        updateAlarmsListView();
                    });
                    setGraphic(deleteButton);
                    deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12px;");
                }
            }
        });
    }
}

