package com.example.demo;

import javafx.scene.control.ListCell;

public class FormatListCell extends ListCell<Integer> {
    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(String.format("%02d", item));
        }
    }
}

