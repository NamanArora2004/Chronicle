package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Community> options;
    FirebaseRecyclerAdapter<Community, MyViewHolder> adapter;
    DatabaseReference communityRef, userFollowingRef;
    FirebaseAuth auth;
    FloatingActionButton floatingActionButton;
    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        floatingActionButton = findViewById(R.id.floatingbutton);
        searchInput = findViewById(R.id.inputsearch);

        // Initialize Firebase references and auth
        communityRef = FirebaseDatabase.getInstance().getReference().child("Community");
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        userFollowingRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Following");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Load all communities initially
        loadCommunities("");

        // Handle search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadCommunities(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }

    private void loadCommunities(String queryText) {
        Query query = communityRef.orderByChild("CommunityName").startAt(queryText).endAt(queryText + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Community>().setQuery(query, Community.class).build();

        adapter = new FirebaseRecyclerAdapter<Community, MyViewHolder>(options) {
            private boolean isColored = false;

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Community model) {
                holder.communityName.setText(model.getCommunityName());
                holder.Description.setText(model.getDescription());
                holder.shopName.setText(model.getShopName());
                Picasso.get().load(model.getImageUrl()).into(holder.communityImage);
                Picasso.get().load(model.getShopImageUrl()).into(holder.shopImage);

                String communityKey = getRef(position).getKey();

                // Check if user is already following this community
                userFollowingRef.child(communityKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.followButton.setText("Following");
                        } else {
                            holder.followButton.setText("Follow");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                // Handle follow/unfollow button click
                holder.followButton.setOnClickListener(v -> {
                    userFollowingRef.child(communityKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Unfollow if already following
                                userFollowingRef.child(communityKey).removeValue();
                                holder.followButton.setText("Follow");
                                Toast.makeText(HomeActivity.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                            } else {
                                // Follow if not already following
                                Map<String, Object> followData = new HashMap<>();
                                followData.put("communityName", model.getCommunityName());
                                followData.put("communityImageUrl", model.getImageUrl());

                                userFollowingRef.child(communityKey).setValue(followData);
                                holder.followButton.setText("Following");
                                Toast.makeText(HomeActivity.this, "Followed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                });

                // View community details
                View.OnClickListener onClickListener = v -> {
                    Intent intent = new Intent(HomeActivity.this, ViewActivity.class);
                    intent.putExtra("CommunityKey", communityKey);
                    startActivity(intent);
                };

                holder.communityName.setOnClickListener(onClickListener);
                holder.Description.setOnClickListener(onClickListener);
                holder.communityImage.setOnClickListener(onClickListener);

                holder.likeButton.setOnClickListener(v -> {
                    if (isColored) {
                        // Change to black color
                        holder.likeButton.setColorFilter(v.getContext().getResources().getColor(android.R.color.black));
                    } else {
                        // Change to white color
                        holder.likeButton.setColorFilter(v.getContext().getResources().getColor(android.R.color.white));
                        Toast.makeText(HomeActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                    }
                    isColored = !isColored;
                });

                holder.qnaButton.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, QnAActivity.class);
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    // ViewHolder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView communityName, Description, shopName;
        ImageView communityImage, shopImage, likeButton, qnaButton;
        AppCompatButton followButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views based on your layout
            communityImage = itemView.findViewById(R.id.imagemmunity);
            communityName = itemView.findViewById(R.id.tv1);
            shopName = itemView.findViewById(R.id.tv3);
            followButton = itemView.findViewById(R.id.follow);
            Description = itemView.findViewById(R.id.tv2);
            shopImage = itemView.findViewById(R.id.image_shop);
            likeButton = itemView.findViewById(R.id.like);
            qnaButton = itemView.findViewById(R.id.qna);
        }
    }
}
