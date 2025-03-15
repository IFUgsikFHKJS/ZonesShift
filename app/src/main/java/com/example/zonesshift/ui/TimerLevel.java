package com.example.zonesshift.ui;

public class TimerLevel {

    private long startTime;
    private long currentTime;

    public void startTimer(){
        startTime = System.currentTimeMillis();
        currentTime = startTime;
    }

    public String tickTimer(double delta){
//        System.out.println(delta);
        currentTime += delta * 1000;
        long currentTimeDelta =  currentTime - startTime;

        if (currentTimeDelta >= 39599008)
            return "69:69:69";


        System.out.println(currentTimeDelta);
        long minutes = (currentTimeDelta / 60000) % 60;
        long seconds = (currentTimeDelta / 1000) % 60;
        long hundredths = (currentTimeDelta / 10) % 100; // Десятые и сотые доли секунды




        return String.format("%02d:%02d:%02d", minutes, seconds, hundredths);
    }

}
