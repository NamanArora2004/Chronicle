package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QnaHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton addQuestionButton;
    private List<Question> questionList;
    private QuestionAdapter questionAdapter;
    private DatabaseReference questionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qna_home2);

        recyclerView = findViewById(R.id.qna_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addQuestionButton = findViewById(R.id.fab_add_question);

        // Initialize Firebase reference
        questionRef = FirebaseDatabase.getInstance().getReference("Questions");

        // Initialize question list
        questionList = new ArrayList<>();

        // Adapter for questions
        questionAdapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(questionAdapter);

        // Load questions from Firebase and update adapter
        loadQuestions();

        // Floating button click to add a new question
        addQuestionButton.setOnClickListener(v -> {
            Intent intent = new Intent(QnaHomeActivity.this, AddQuestionActivity.class);
            startActivity(intent);
        });
    }


    private void loadQuestions() {
        // Load questions from Firebase
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questionList.add(question); // Add each question to the list
                }
                questionAdapter.notifyDataSetChanged(); // Notify the adapter to update RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(QnaHomeActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
