package com.example.zonesshift.authentication;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserInfo {

    private static String username = null;

    public static void getUserName(final UserNameCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid(); // Get UID of the current logged-in user
            DocumentReference userRef = db.collection("users").document(userId); // Path to the user's document

            // Fetch the user's document
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Extract the username from the Firestore document
                    String username = task.getResult().getString("username");
                    if (username != null) {
                        callback.onUserNameReceived(username); // Pass the username to callback
                    } else {
                        callback.onError("Username not found.");
                    }
                } else {
                    callback.onError("Failed to retrieve username.");
                }
            });
        } else {
            callback.onError("User is not logged in.");
        }
    }

    // Callback interface to handle the result or error
    public interface UserNameCallback {
        void onUserNameReceived(String username);
        void onError(String error);
    }


    public interface UserIdCallback {
        void onUserIdReceived(int userId);
        void onError(String error);
    }

    public static void getUserId(UserIdCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            callback.onError("User not authenticated");
            return;
        }

        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists() && document.contains("user_id")) {
                        int userId = document.getLong("user_id").intValue();
                        callback.onUserIdReceived(userId);
                    } else {
                        callback.onError("User ID not found");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}

