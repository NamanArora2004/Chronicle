package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignPage extends AppCompatActivity {

    // Declare your UI elements
    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private TextView forgotPassword, registerText;
    private FirebaseAuth mAuth;
    private ImageView facebookLogin, googleLogin;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainPage_FirstPage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_page);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Connect the UI elements from XML with Java
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.cirLoginButton);
        forgotPassword = findViewById(R.id.textViewForgotPassword);
        registerText = findViewById(R.id.textViewRegister);

        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignPage.this, ForgotPassswordActivity.class);
                        startActivity(intent);

                    }
                });
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignPage.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform Firebase sign-in
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        // Navigate to the main page after successful login
                                        Intent intent = new Intent(SignPage.this, MainPage_FirstPage.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignPage.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Handle "Forgot Password?" text click
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle forgot password action
                Toast.makeText(SignPage.this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignPage.this, ForgotPassswordActivity.class); // Replace RegisterPage with your actual registration activity
                startActivity(intent);

                // Add logic for password recovery (if needed)
            }
        });

        // Handle "New user? Register Now" text click
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the registration page
                Toast.makeText(SignPage.this, "Redirecting to Register page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignPage.this, RegisterPage.class); // Replace RegisterPage with your actual registration activity
                startActivity(intent);
            }
        });
    }
}
