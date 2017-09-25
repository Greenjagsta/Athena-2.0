package com.athena.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {
    private final DateFormat dateFormat;
    private long startTime = 0;
    private long endTime = 0;

    private Date startDate;
    private Date endDate;

    public Timer() {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    public void startTimer() {
        startTime = System.nanoTime();
        startDate = new Date();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
        endDate = new Date();
    }

    public long getElapsedTimeSeconds() {
        if (endTime == 0) {
            return (long) ((System.nanoTime() - startTime) / 1E9);
        }
        return (long) ((endTime - startTime) / 1E9);
    }

    public long getElapsedTimeExact() {
        if (endTime == 0) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }

    public String getStartDate() {
        return dateFormat.format(startDate);
    }

    public String getEndDate() {
        return dateFormat.format(endDate);
    }

    public void resetTimer() {
        this.startTime = 0;
        this.endTime = 0;
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        int hours = minutes / 60;
        minutes -= hours * 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}