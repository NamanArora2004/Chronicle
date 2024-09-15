package com.example.myapplication100;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passsword);

        emailInput = findViewById(R.id.emailInput);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        // Reset password button listener
        resetPasswordBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgotPassswordActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase method to send password reset email
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            Toast.makeText(ForgotPassswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
