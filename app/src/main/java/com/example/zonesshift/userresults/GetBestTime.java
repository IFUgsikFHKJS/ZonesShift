package com.example.zonesshift.userresults;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetBestTime {

    public static void getBestTimeForUser(int userId, int mapId, OnSuccessListener<String> onSuccess, OnFailureListener onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("leaderboard")
                .document(String.valueOf(mapId)) // Документ карты
                .collection("results") // Вложенная коллекция результатов
                .document(String.valueOf(userId)) // Документ с временем конкретного игрока
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String bestTime = documentSnapshot.getString("time");

                        if (bestTime != null) {
                            onSuccess.onSuccess(bestTime);
                        } else {
                            onFailure.onFailure(new Exception("Best time not found"));
                        }
                    } else {
                        onFailure.onFailure(new Exception("No record found for this user and map"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

}
