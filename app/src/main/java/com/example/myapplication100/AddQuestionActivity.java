package com.example.myapplication100;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddQuestionActivity extends AppCompatActivity {

    EditText questionText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        questionText = findViewById(R.id.et_question_text);
        submitButton = findViewById(R.id.btn_submit_question);

        submitButton.setOnClickListener(v -> {
            String question = questionText.getText().toString().trim();

            if (!question.isEmpty()) {
                // Create a new Question object
                String questionId = FirebaseDatabase.getInstance().getReference().push().getKey(); // Unique ID for the question
                Question newQuestion = new Question(questionId, question, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), new HashMap<>());

                // Store the question in Firebase
                FirebaseDatabase.getInstance().getReference("Questions").child(questionId).setValue(newQuestion)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddQuestionActivity.this, "Question added successfully!", Toast.LENGTH_SHORT).show();

                                // Notify community members
                                notifyCommunityMembers(newQuestion);

                                // Go back to the Q&A Home page
                                finish();
                            } else {
                                Toast.makeText(AddQuestionActivity.this, "Failed to add question. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(AddQuestionActivity.this, "Please enter a question", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifyCommunityMembers(Question question) {
        // You can implement Firebase Cloud Messaging (FCM) to send notifications to community members
        // or store the notification details in Firebase under a "Notifications" node for the app to read
    }
}
