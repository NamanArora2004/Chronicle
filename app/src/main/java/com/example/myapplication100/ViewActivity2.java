package com.example.myapplication100;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewActivity2 extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    TextView textView2;
    TextView textView3;
    Button button;

    ScrollView scrollView;
    DatabaseReference blogdataref;
    DatabaseReference userFollowingRef;
    FirebaseAuth auth;
    String blogKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view2);

        imageView = findViewById(R.id.imageBlog);
        scrollView = findViewById(R.id.scrollview);
        textView = findViewById(R.id.textview);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        button = findViewById(R.id.followButton);

        // Initialize Firebase Auth and Database references
        auth = FirebaseAuth.getInstance();
        blogdataref = FirebaseDatabase.getInstance().getReference().child("Blogs");
        userFollowingRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(auth.getCurrentUser().getUid()).child("FollowingBlogs");

        // Get blog key from Intent
        blogKey = getIntent().getStringExtra("BlogsKey");

        // Load blog details
        loadBlogDetails();

        // Set up follow button functionality
        setupFollowButton();
    }

    private void loadBlogDetails() {
        blogdataref.child(blogKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String author = snapshot.child("AuthorName").getValue(String.class);
                    String title = snapshot.child("BlogTitle").getValue(String.class);
                    String content = snapshot.child("Content").getValue(String.class);
                    String imageUrl = snapshot.child("BlogImageUrl").getValue(String.class);

                    Picasso.get().load(imageUrl).into(imageView);
                    textView.setText(author);
                    textView2.setText(title);
                    textView3.setText(content);

                    // Check if the user is already following the blog
                    checkIfFollowing();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void setupFollowButton() {
        button.setOnClickListener(v -> {
            userFollowingRef.child(blogKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // User is already following the blog
                        userFollowingRef.child(blogKey).removeValue().addOnSuccessListener(unused -> {
                            button.setText("Follow");
                            Toast.makeText(ViewActivity2.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // User is not following the blog
                        userFollowingRef.child(blogKey).setValue(true).addOnSuccessListener(unused -> {
                            button.setText("Unfollow");
                            Toast.makeText(ViewActivity2.this, "Followed", Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors
                }
            });
        });
    }

    private void checkIfFollowing() {
        userFollowingRef.child(blogKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    button.setText("Unfollow");
                } else {
                    button.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}
