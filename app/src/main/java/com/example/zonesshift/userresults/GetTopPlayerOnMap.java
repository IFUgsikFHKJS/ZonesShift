//package com.example.zonesshift.userresults;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class GetTopPlayerOnMap {
//
//    public static void getTopPlayers(String mapId, OnSuccessListener<Map<String, String>> onSuccess, OnFailureListener onFailure) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("leaderboard")
//                .document(mapId)
//                .collection("results")
//                .orderBy("time")
//                .limit(10)
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    if (querySnapshot.isEmpty()) {
//                        onFailure.onFailure(new Exception("No results found"));
//                        return;
//                    }
//
//                    Map<String, String> topPlayers = new LinkedHashMap<>();
//                    List<Task<DocumentSnapshot>> userTasks = new ArrayList<>();
//
//                    for (QueryDocumentSnapshot doc : querySnapshot) {
//                        System.out.println(topPlayers);
//
//                        String userId = doc.getString("user_id");
//                        String time = doc.getString("time");
//
//                            Task<DocumentSnapshot> userTask = db.collection("users").whereEqualTo("user_id", userId).limit(1).get();
//                            userTasks.add(userTask);
//
//                            userTask.addOnSuccessListener(userSnapshot -> {
//                                String username = userSnapshot.exists() ? userSnapshot.getString("username") : "Unknown";
//                                topPlayers.put(username, time);
//
//                            });
//                    }
//
//
//                    // Дожидаемся выполнения всех запросов к users
//                    Tasks.whenAllComplete(userTasks).addOnSuccessListener(tasks -> onSuccess.onSuccess(topPlayers))
//                            .addOnFailureListener(onFailure);
//                })
//                .addOnFailureListener(onFailure);
//    }
//
//}
