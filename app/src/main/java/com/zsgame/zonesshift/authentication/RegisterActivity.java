package com.zsgame.zonesshift.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zsgame.zonesshift.R;
import com.zsgame.zonesshift.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText email, username, password;
    private Button btnRegister, btnVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        btnVerification = findViewById(R.id.btnVerification);

        btnRegister.setOnClickListener(v -> {
            String txt_email = email.getText().toString().trim();
            String txt_username = username.getText().toString().trim();
            String txt_password = password.getText().toString().trim();
            if (txt_email.isEmpty() || txt_password.isEmpty() || txt_username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txt_email, txt_password, txt_username);
            }
        });

        btnVerification.setOnClickListener(v -> checkEmailVerification());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void registerUser(String email, String password, String username) {

        if (!isValidUsername(username)) {
            Toast.makeText(RegisterActivity.this, "Username can only contain letters and numbers (A-Z, a-z, 0-9).", Toast.LENGTH_SHORT).show();
            return;
        }

        if(username.length() > 12){
            Toast.makeText(RegisterActivity.this, "Username cannot be more than 12 letters long", Toast.LENGTH_SHORT).show();
            return;
        }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();

                            // Получаем максимальный user_id
                            db.collection("users").orderBy("user_id", Query.Direction.DESCENDING).limit(1).get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        int newUserId = 1; // По умолчанию первый ID

                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            DocumentSnapshot lastUser = queryDocumentSnapshots.getDocuments().get(0);
                                            newUserId = lastUser.getLong("user_id").intValue() + 1; // Увеличиваем ID
                                        }

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("username", username);
                                        user.put("email", email);
                                        user.put("user_id", newUserId);

                                        db.collection("users").document(userId).set(user)
                                                .addOnSuccessListener(aVoid -> {
                                                    mAuth.getCurrentUser().sendEmailVerification()
                                                            .addOnCompleteListener(emailTask -> {
                                                                if (emailTask.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                })
                                                .addOnFailureListener(e ->
                                                        Toast.makeText(RegisterActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show()
                                                );
                                    });

                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration Failed";
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$"; // Only A-Z, a-z, 0-9
        return username.matches(regex);
    }

    public static void updateUserIDs(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int counter = 1; // Начальный ID (можно взять из последнего зарегистрированного пользователя)
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                if (!document.contains("user_id")) { // Проверяем, есть ли уже user_id
                    db.collection("users").document(document.getId()).update("user_id", counter)
                            .addOnSuccessListener(aVoid -> System.out.println("Updated user: " + document.getId()))
                            .addOnFailureListener(e -> System.out.println("Error updating user: " + e.getMessage()));
                    counter++;
                }
            }
        });
    }


    private void checkEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                if (user.isEmailVerified()) {
                    Toast.makeText(this, "Email Verified! Logging in...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Email is not verified yet. Please check your inbox.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}