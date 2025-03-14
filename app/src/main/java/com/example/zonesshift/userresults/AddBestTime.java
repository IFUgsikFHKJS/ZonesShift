package com.example.zonesshift.userresults;

import com.example.zonesshift.helpers.interfaces.TimeComparator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBestTime {

    public static void saveBestTime(int userId, int mapId, String newTime) {
        System.out.println("UserId " + userId  + " mapID " + mapId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userResultRef = db.collection("leaderboard")
                .document(String.valueOf(mapId))  // Преобразуем mapId в String
                .collection("results")
                .document(String.valueOf(userId));  // Преобразуем userId в String

        userResultRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String currentBestTime = documentSnapshot.getString("time");

                if (TimeComparator.compareTimes(newTime, currentBestTime) < 0) { // Если новое время лучше
                    userResultRef.update("time", newTime)
                            .addOnSuccessListener(aVoid -> System.out.println("Best time updated"))
                            .addOnFailureListener(e -> System.out.println("Failed to update time"));
                }
            } else {
                // Если у игрока еще нет записи, создаем новую
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("userId", String.valueOf(userId));
                resultData.put("time", newTime);

                userResultRef.set(resultData)
                        .addOnSuccessListener(aVoid -> System.out.println("New best time saved"))
                        .addOnFailureListener(e -> System.out.println("Failed to save new time"));
            }
        });
    }

}
