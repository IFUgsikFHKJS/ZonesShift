package com.zsgame.zonesshift.environments.mapmanagment.mapcreating;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoadMap {


    public static void getMapById(int mapId,
                                  OnSuccessListener<Map<String, Object>> onSuccess,
                                  OnFailureListener onFailure) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("maps")
                .whereEqualTo("map_id", mapId)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        onSuccess.onSuccess(document.getData());
                    } else {
                        onFailure.onFailure(new Exception("Map with id " + mapId + " not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

}
