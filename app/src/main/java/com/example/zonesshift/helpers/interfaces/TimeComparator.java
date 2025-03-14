package com.example.zonesshift.helpers.interfaces;

public interface TimeComparator {

    static int compareTimes(String time1, String time2) {
        String[] parts1 = time1.split(":");
        String[] parts2 = time2.split(":");

        int minutes1 = Integer.parseInt(parts1[0]);
        int seconds1 = Integer.parseInt(parts1[1]);
        int milliseconds1 = Integer.parseInt(parts1[2]);

        int minutes2 = Integer.parseInt(parts2[0]);
        int seconds2 = Integer.parseInt(parts2[1]);
        int milliseconds2 = Integer.parseInt(parts2[2]);

        if (minutes1 != minutes2) return Integer.compare(minutes1, minutes2);
        if (seconds1 != seconds2) return Integer.compare(seconds1, seconds2);
        return Integer.compare(milliseconds1, milliseconds2);
    }
}
