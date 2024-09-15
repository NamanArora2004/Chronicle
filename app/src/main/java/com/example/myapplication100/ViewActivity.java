package com.example.myapplication100;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {

    ImageView communityImage ,shopimg;
    TextView communityName, communityDescription,shopname;
    FloatingActionButton floatingActionButton;
    ScrollView scrollView;
    DatabaseReference communityRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Initialize views
        communityImage = findViewById(R.id.communityImage);

        scrollView = findViewById(R.id.scrollview);
        communityName = findViewById(R.id.textview1);
        communityDescription = findViewById(R.id.textview2);
        floatingActionButton = findViewById(R.id.floatingbtn);

        // Get Firebase Database reference for "Community"
        communityRef = FirebaseDatabase.getInstance().getReference().child("Community");

        // Get the key of the community from the intent
        String communityKey = getIntent().getStringExtra("CommunityKey");

        if (communityKey != null) {
            loadCommunityDetails(communityKey);
        } else {
            // Handle case when the community key is null or not passed properly
            communityName.setText("No community found");
        }
    }

    private void loadCommunityDetails(String communityKey) {
        // Listen for changes in the specific community based on the key
        communityRef.child(communityKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve community details from Firebase
                    String communityNameValue = snapshot.child("CommunityName").getValue(String.class);
                    String descriptionValue = snapshot.child("Description").getValue(String.class);
                    String imageUrl = snapshot.child("ImageUrl").getValue(String.class);


                    // Update UI elements
                    if (communityNameValue != null) {
                        communityName.setText(communityNameValue);
                    }
                    if (descriptionValue != null) {
                        communityDescription.setText(descriptionValue);
                    }
                    if (imageUrl != null) {
                        Picasso.get().load(imageUrl).into(communityImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors while retrieving data from Firebase
                communityName.setText("Error loading data");
            }
        });
    }
}
