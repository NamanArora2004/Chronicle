package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
Button login=findViewById(R.id.LoginBtn);
        EditText username, email, phone, password;
        username = findViewById(R.id.Name);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Mobile);
        password = findViewById(R.id.Password);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        Button registerBtn = findViewById(R.id.RegisterButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailstored = email.getText().toString();
                String passwordstored = password.getText().toString();
                String usernamestored = username.getText().toString();
                String phonestored = phone.getText().toString();

                if (!isValidEmail(emailstored)) {
                    Toast.makeText(RegisterPage.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(passwordstored)) {
                    Toast.makeText(RegisterPage.this, "Password must be at least 8 characters long, include a number, a letter, and a special character.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhoneNumber(phonestored)) {
                    Toast.makeText(RegisterPage.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailstored, passwordstored).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            // Store additional user data in Firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> userData = new HashMap<>();
                            userData.put("User Name", usernamestored);
                            userData.put("Email", emailstored);
                            userData.put("Phone Number", phonestored);

                            databaseReference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterPage.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent =new Intent(RegisterPage.this, MainPage_FirstPage.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterPage.this, "Failed to store user data!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterPage.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public boolean isValidEmail(String email) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                return email != null && email.matches(emailPattern);
            }

            public boolean isValidPassword(String password) {
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,}$";
                return password != null && password.matches(passwordPattern);
            }

            public boolean isValidPhoneNumber(String phone) {
                String phonePattern = "^[0-9]{10}$";
                return phone != null && phone.matches(phonePattern);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterPage.this, SignPage.class);
                startActivity(intent);
            }
        });

    }
}
