package com.example.zonesshift.helpers.interfaces;

import com.example.zonesshift.main.GamePanel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public interface Verifications {

    default void saveVerifications(Map<String, Boolean> map) {
        JSONObject obj = new JSONObject(map);
        File file = new File(GamePanel.getGameContext().getFilesDir(), "verifications.json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default Map<String, Boolean> loadVerifications() {
        File file = new File(GamePanel.getGameContext().getFilesDir(), "verifications.json");

        if (!file.exists()) return new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            JSONObject obj = new JSONObject(json.toString());
            Map<String, Boolean> result = new HashMap<>();
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                result.put(key, obj.getBoolean(key));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
