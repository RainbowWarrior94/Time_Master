package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class AlarmList {
    private static final List<String> alarms = new ArrayList<>();

    public static List<String> getAlarms() {
        return alarms;
    }

    public static void addAlarm(String alarm) {
        alarms.add(alarm);
    }

    public static void removeAlarm(String alarm) {
        alarms.remove(alarm);
    }
}
