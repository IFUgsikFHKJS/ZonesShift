package com.zsgame.zonesshift.userresults;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetTopPlayerOnMap {

    public static void getTopPlayers(String mapId, OnSuccessListener<Map<String, String>> onSuccess, OnFailureListener onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("leaderboard")
                .document(mapId)
                .collection("results")
                .orderBy("time")
                .limit(11)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        onFailure.onFailure(new Exception("No results found"));
                        return;
                    }

                    Map<String, String> topPlayers = new LinkedHashMap<>();
                    List<Task<DocumentSnapshot>> userTasks = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String userId = doc.getString("userId");
                        String time = doc.getString("time");

                        Task<DocumentSnapshot> userTask = FirebaseFirestore.getInstance()
                                .collection("users")
                                .whereEqualTo("user_id", Integer.parseInt(userId))
                                .limit(1)
                                .get()
                                .continueWith(task -> task.getResult().isEmpty() ? null : task.getResult().getDocuments().get(0));

                        userTasks.add(userTask);

                        userTask.addOnSuccessListener(userSnapshot -> {
                            if (userSnapshot != null) {
                                String username = userSnapshot.getString("username");
                                topPlayers.put(username, time);
                            }
                        });
                    }

                    // Ждем завершения всех запросов
                    Tasks.whenAllSuccess(userTasks).addOnSuccessListener(results -> onSuccess.onSuccess(topPlayers))
                            .addOnFailureListener(onFailure);
                })
                .addOnFailureListener(onFailure);
    }


    public static Map<String, String> sortPlayersByTime(Map<String, String> players) {
        return players.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> parseTimeToMillis(e.getValue())))
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

    // Метод для преобразования строки времени в миллисекунды
    private static int parseTimeToMillis(String time) {
        try {
            String[] parts = time.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            int milliseconds = Integer.parseInt(parts[2]);

            return (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
    }

}
