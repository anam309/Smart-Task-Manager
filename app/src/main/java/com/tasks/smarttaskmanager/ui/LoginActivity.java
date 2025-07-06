package com.tasks.smarttaskmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.tasks.smarttaskmanager.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginBtn, signupBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.edit_email);
        passwordField = findViewById(R.id.edit_password);
        loginBtn = findViewById(R.id.btn_login);
        signupBtn = findViewById(R.id.btn_signup);

        loginBtn.setOnClickListener(v -> signIn());
        signupBtn.setOnClickListener(v -> signUp());

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void signIn() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}