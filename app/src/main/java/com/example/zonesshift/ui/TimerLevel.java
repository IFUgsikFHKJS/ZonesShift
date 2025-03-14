package com.example.zonesshift.ui;

public class TimerLevel {

    private long startTime;

    public void startTimer(){
        startTime = System.currentTimeMillis();
    }

    public String tickTimer(){
        long currentTime =  System.currentTimeMillis() - startTime;
        long minutes = (currentTime / 60000) % 60;
        long seconds = (currentTime / 1000) % 60;
        long hundredths = (currentTime / 10) % 100; // Десятые и сотые доли секунды

        return String.format("%02d:%02d:%02d", minutes, seconds, hundredths);
    }

}
