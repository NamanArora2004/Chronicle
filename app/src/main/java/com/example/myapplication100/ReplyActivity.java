package com.example.myapplication100;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReplyActivity extends AppCompatActivity {

    TextView questionText;
    EditText replyText;
    Button submitReplyButton;
    RecyclerView repliesRecyclerView;
    String questionId;

    List<String> repliesList;
    ReplyAdapter replyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        // Initialize views
        questionText = findViewById(R.id.tv_question_text);
        replyText = findViewById(R.id.et_reply_text);
        submitReplyButton = findViewById(R.id.reply_button);
        repliesRecyclerView = findViewById(R.id.replies_recycler_view);

        // Set up RecyclerView
        repliesList = new ArrayList<>();
        replyAdapter = new ReplyAdapter(repliesList);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repliesRecyclerView.setAdapter(replyAdapter);

        // Get question ID from intent
        questionId = getIntent().getStringExtra("questionId");

        if (questionId == null) {
            Toast.makeText(this, "Invalid question ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load the question from Firebase
        FirebaseDatabase.getInstance().getReference("Questions").child(questionId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Question question = snapshot.getValue(Question.class);
                        if (question != null) {
                            questionText.setText(question.getQuestionText());
                            // Load replies
                            loadReplies();
                        } else {
                            Toast.makeText(ReplyActivity.this, "Question not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReplyActivity.this, "Failed to load question", Toast.LENGTH_SHORT).show();
                    }
                });

        // Submit reply to Firebase
        submitReplyButton.setOnClickListener(v -> {
            submitReply();
        });
    }

    private void submitReply() {
        String reply = replyText.getText().toString().trim();

        if (!reply.isEmpty()) {
            // Add the reply to the question in Firebase
            FirebaseDatabase.getInstance().getReference("Questions").child(questionId)
                    .child("replies").push().setValue(reply)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ReplyActivity.this, "Reply submitted!", Toast.LENGTH_SHORT).show();
                            replyText.setText(""); // Clear the input field

                            // Reload replies after submission
                            loadReplies();
                        } else {
                            Toast.makeText(ReplyActivity.this, "Failed to submit reply. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(ReplyActivity.this, "Please enter a reply", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadReplies() {
        FirebaseDatabase.getInstance().getReference("Questions").child(questionId).child("replies")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        repliesList.clear(); // Clear the list to avoid duplicates
                        for (DataSnapshot replySnapshot : snapshot.getChildren()) {
                            String reply = replySnapshot.getValue(String.class);
                            repliesList.add(reply);
                        }
                        replyAdapter.notifyDataSetChanged(); // Update the RecyclerView
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReplyActivity.this, "Failed to load replies", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
