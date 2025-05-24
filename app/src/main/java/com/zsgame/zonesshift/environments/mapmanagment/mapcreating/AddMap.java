package com.zsgame.zonesshift.environments.mapmanagment.mapcreating;

import android.util.Log;
import android.widget.Toast;

import com.zsgame.zonesshift.main.GamePanel;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.zsgame.zonesshift.environments.Tile;
import com.google.firebase.firestore.Query;

public class AddMap {

    public static void addMap(Tile[][] tiles, String lvlName, int author_id, String goldTime, String silverTime, String bronzeTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mapTiles = mapToString(tiles);

        // Сначала проверяем, существует ли карта с таким именем
        db.collection("maps")
                .whereEqualTo("name", lvlName)
                .get()
                .addOnSuccessListener(nameQuery -> {
                    if (!nameQuery.isEmpty()) {
                        Log.w("Firestore", "Карта с таким именем уже существует: " + lvlName);
                        return; // Не добавляем карту с дублирующим именем
                    }

                    // Если имя уникальное — найдём последний map_id
                    db.collection("maps")
                            .orderBy("map_id", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener(idQuery -> {
                                int mapId; // Начинаем с 100
                                int lastID = idQuery.getDocuments().get(0).getLong("map_id").intValue();

                                if (!idQuery.isEmpty() && lastID >= 100) {
                                    mapId = lastID + 1;
                                } else {
                                    mapId = 100;
                                }

                                // Формируем данные карты
                                Map<String, Object> mapData = new HashMap<>();
                                mapData.put("map_id", mapId);
                                mapData.put("name", lvlName);
                                mapData.put("author_id", author_id);
                                mapData.put("map_data", mapTiles);
                                mapData.put("created_at", FieldValue.serverTimestamp());
                                mapData.put("gold_time", goldTime);
                                mapData.put("silver_time", silverTime);
                                mapData.put("bronze_time", bronzeTime);
                                mapData.put("likes", 0);
                                mapData.put("dislikes", 0);

                                db.collection("maps")
                                        .add(mapData)
                                        .addOnSuccessListener(documentReference -> Log.d("Firestore", "Map added with ID: " + mapId))
                                        .addOnFailureListener(e -> Log.w("Firestore", "Ошибка при добавлении карты", e));
                            })
                            .addOnFailureListener(e -> Log.w("Firestore", "Ошибка при получении последнего map_id", e));
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Ошибка при проверке уникальности имени", e));
    }

    public static void addMap(String mapTiles, String lvlName, int author_id, String goldTime, String silverTime, String bronzeTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Сначала проверяем, существует ли карта с таким именем
        db.collection("maps")
                .whereEqualTo("name", lvlName)
                .get()
                .addOnSuccessListener(nameQuery -> {
                    if (!nameQuery.isEmpty()) {
                        Toast.makeText(GamePanel.getGameContext(), "A map with this name already exists.", Toast.LENGTH_SHORT).show();

                        return; // Не добавляем карту с дублирующим именем
                    }

                    // Если имя уникальное — найдём последний map_id
                    db.collection("maps")
                            .orderBy("map_id", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener(idQuery -> {
                                int mapId; // Начинаем с 100
                                int lastID = idQuery.getDocuments().get(0).getLong("map_id").intValue();

                                if (!idQuery.isEmpty() && lastID >= 100) {
                                    mapId = lastID + 1;
                                } else {
                                    mapId = 100;
                                }

                                // Формируем данные карты
                                Map<String, Object> mapData = new HashMap<>();
                                mapData.put("map_id", mapId);
                                mapData.put("name", lvlName);
                                mapData.put("author_id", author_id);
                                mapData.put("map_data", mapTiles);
                                mapData.put("created_at", FieldValue.serverTimestamp());
                                mapData.put("gold_time", goldTime);
                                mapData.put("silver_time", silverTime);
                                mapData.put("bronze_time", bronzeTime);
                                mapData.put("likes", 0);
                                mapData.put("dislikes", 0);

                                db.collection("maps")
                                        .add(mapData)
                                        .addOnSuccessListener(documentReference -> Toast.makeText(GamePanel.getGameContext(), "Map added with ID: " + mapId, Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Log.w("Firestore", "Ошибка при добавлении карты", e));
                            })
                            .addOnFailureListener(e -> Log.w("Firestore", "Ошибка при получении последнего map_id", e));
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Ошибка при проверке уникальности имени", e));
    }


    private static String mapToString(Tile[][] map) {
        StringBuilder sb = new StringBuilder();
        for (Tile[] row : map) {
            for (Tile tile : row) {
                if (tile == null)
                    sb.append('.');
                else
                    sb.append(tile.getType());
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

}
