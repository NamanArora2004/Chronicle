package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QnAActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton addQuestionButton;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qn_aactivity);

        recyclerView = findViewById(R.id.recycler_view_questions);
        addQuestionButton = findViewById(R.id.fab_add_question);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(questionAdapter);

        // Load questions from Firebase
        loadQuestionsFromFirebase();

        // Add new question
        addQuestionButton.setOnClickListener(v -> {
            // Navigate to AddQuestionActivity to add a new question
            Intent intent = new Intent(QnAActivity.this, AddQuestionActivity.class);
            startActivity(intent);
        });

        // Set up the item click listener
        questionAdapter.setOnItemClickListener(new QuestionAdapter.OnItemClickListener() {
            @Override
            public void onReplyClick(int position) {
                // Handle the reply click, maybe start a new activity for replying
                Question question = questionList.get(position);
                Intent intent = new Intent(QnAActivity.this, ReplyActivity.class);
                intent.putExtra("questionId", question.getQuestionId());
                startActivity(intent);
            }
        });
    }

    private void loadQuestionsFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Questions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        questionList.clear();
                        for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                            Question question = questionSnapshot.getValue(Question.class);
                            if (question != null) {
                                questionList.add(question);
                            }
                        }
                        questionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QnAActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
