package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Blogs_Home extends AppCompatActivity {

    private FirebaseRecyclerOptions<Blogs> options;
    private FirebaseRecyclerAdapter<Blogs, MyViewHolder_Blogs> adapter;
    private DatabaseReference blogDataRef;
    private EditText inputSearch;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs_home);

        // Initialize views
        inputSearch = findViewById(R.id.inputsearch);
        recyclerView = findViewById(R.id.recyclerview_blogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Setup Floating Action Button for blog post creation
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingbutton);
        floatingActionButton.setOnClickListener(v -> {
            // Navigate to Blog_Post activity
            Intent intent = new Intent(Blogs_Home.this, Blog_Post.class);
            startActivity(intent);
        });

        // Firebase reference to the "Blogs" node
        blogDataRef = FirebaseDatabase.getInstance().getReference().child("Blogs");

        // Initially load all blogs without filtering
        loadData("");

        // Set up the search filter using TextWatcher
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action required here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action required here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Update search results based on input
                loadData(s.toString());
            }
        });
    }

    private void loadData(String queryText) {
        // Create a Firebase query to filter blogs by "BlogTitle"
        Query query = blogDataRef.orderByChild("BlogTitle")
                .startAt(queryText)
                .endAt(queryText + "\uf8ff");

        // Configure FirebaseRecyclerOptions with the query
        options = new FirebaseRecyclerOptions.Builder<Blogs>()
                .setQuery(query, Blogs.class)
                .build();

        // Stop listening to the old adapter if it exists
        if (adapter != null) {
            adapter.stopListening();
        }

        // Set up FirebaseRecyclerAdapter
        adapter = new FirebaseRecyclerAdapter<Blogs, MyViewHolder_Blogs>(options) {
            @NonNull
            @Override
            public MyViewHolder_Blogs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the blog item view for each blog post
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_view_blogs, parent, false);
                return new MyViewHolder_Blogs(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder_Blogs holder, int position, @NonNull Blogs model) {
                // Bind blog data to the view holder
                holder.getTextViewTitle().setText(model.getTitle());
                holder.getTextViewAuthor().setText(model.getAuthorName());
                Picasso.get()
                        .load(model.getBlogImageurl())
                        .placeholder(R.drawable.baseline_add_24) // Placeholder image if blog image URL is missing
                        .into(holder.getImageView());

                // Handle blog item click
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(Blogs_Home.this, ViewActivity2.class);
                    intent.putExtra("BlogsKey", getRef(position).getKey());
                    startActivity(intent);
                });
            }
        };

        // Start listening for real-time updates and bind the adapter to RecyclerView
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening to the adapter when activity becomes visible
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to the adapter when the activity goes out of focus
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
