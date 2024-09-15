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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Community_i_follow extends Fragment {

    private RecyclerView recyclerViewFollowed;
    private DatabaseReference userFollowingRef, communityRef;
    private FirebaseAuth auth;
    private FirebaseRecyclerOptions<Community> options;
    private FirebaseRecyclerAdapter<Community, MyViewHolder> adapter;
    private List<String> followedCommunityKeys = new ArrayList<>();
    private EditText searchField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_i_follow, container, false);

        // Initialize RecyclerView
        recyclerViewFollowed = view.findViewById(R.id.recyclerview);
        recyclerViewFollowed.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFollowed.setHasFixedSize(true);

        // Initialize Firebase references
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        userFollowingRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Following");
        communityRef = FirebaseDatabase.getInstance().getReference().child("Community");

        // Initialize Search Field
        searchField = view.findViewById(R.id.inputsearch);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchCommunities(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Load followed communities
        loadFollowedCommunities();

        return view;
    }

    private void loadFollowedCommunities() {
        // Get the list of communities the user is following
        userFollowingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followedCommunityKeys.clear();  // Clear previous list of keys
                for (DataSnapshot followedCommunity : snapshot.getChildren()) {
                    String communityKey = followedCommunity.getKey();  // Get the community ID
                    followedCommunityKeys.add(communityKey);
                }
                loadCommunityDetails();  // Load community details for followed communities
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadCommunityDetails() {
        if (!followedCommunityKeys.isEmpty()) {
            // Query the communities followed by the user
            Query query = communityRef.orderByKey().startAt(followedCommunityKeys.get(0)).endAt(followedCommunityKeys.get(followedCommunityKeys.size() - 1));
            options = new FirebaseRecyclerOptions.Builder<Community>().setQuery(query, Community.class).build();

            adapter = new FirebaseRecyclerAdapter<Community, MyViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Community model) {
                    String communityKey = getRef(position).getKey();

                    if (followedCommunityKeys.contains(communityKey)) {
                        holder.communityName.setText(model.getCommunityName());
                        holder.shopName.setText(model.getShopName());
                        Picasso.get().load(model.getImageUrl()).into(holder.communityImage);

                        // Handle item click to view details
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), ViewActivity.class);
                            intent.putExtra("CommunityKey", communityKey);
                            startActivity(intent);
                        });
                    } else {
                        holder.itemView.setVisibility(View.GONE); // Hide the view if it's not a followed community
                    }
                }

                @NonNull
                @Override
                public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    // Inflate the layout for each community item (same as 'all communities')
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_comm_follow, parent, false);
                    return new MyViewHolder(view);
                }
            };

            adapter.startListening();
            recyclerViewFollowed.setAdapter(adapter);
        }
    }

    private void searchCommunities(String searchText) {
        Query searchQuery = communityRef.orderByChild("communityName").startAt(searchText).endAt(searchText + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Community>().setQuery(searchQuery, Community.class).build();
        adapter.updateOptions(options);  // Update adapter with new query
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

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
