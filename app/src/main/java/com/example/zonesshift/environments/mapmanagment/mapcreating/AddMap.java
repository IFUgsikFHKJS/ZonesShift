package com.example.zonesshift.environments.mapmanagment.mapcreating;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.example.zonesshift.environments.Tile;
import com.google.firebase.firestore.Query;

public class AddMap {

    public static void addMap(Tile[][] tiles, String lvlName, int author_id, String goldTime, String silverTime, String bronzeTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String mapTiles = mapToString(tiles);

        // Запрашиваем карты, сортируем по map_id в убывающем порядке и ограничиваем 1 результатом
        db.collection("maps")
                .orderBy("map_id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int mapId = 1; // По умолчанию, если карт нет, начинаем с 1

                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot lastMap = queryDocumentSnapshots.getDocuments().get(0);
                        mapId = lastMap.getLong("map_id").intValue() + 1; // Увеличиваем последний map_id
                    }

                    // Создаем данные карты
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

                    // Добавляем карту в Firestore
                    db.collection("maps").add(mapData)
                            .addOnSuccessListener(documentReference -> Log.d("Firestore", "Map added!"))
                            .addOnFailureListener(e -> Log.w("Firestore", "Error adding map", e));
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error fetching last map_id", e));
    }

    private static String mapToString(Tile[][] map) {
        StringBuilder sb = new StringBuilder();
        for (Tile[] row : map) {
            for (Tile tile : row) {
                if (tile == null)
                    sb.append('.');
                else
                    sb.append(tile.getType()); // Получаем символ типа Tile
            }
            sb.append("\n"); // Разделяем строки карты
        }
        return sb.toString().trim(); // Убираем последний перевод строки
    }

}
