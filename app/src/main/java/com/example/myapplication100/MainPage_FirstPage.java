package com.example.myapplication100;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
public class MainPage_FirstPage extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_first_page);

        // Initialize views
        DrawerLayout drawerLayout = findViewById(R.id.main);
        ImageButton imageButton = findViewById(R.id.imagebutton);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FrameLayout frameLayout = findViewById(R.id.framelayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        // Initialize FragmentManager
        fragmentManager = getSupportFragmentManager();

        // Get header view from NavigationView
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.imageofuser);
        TextView textView = headerView.findViewById(R.id.NameofUser);
        TextView textView1 = headerView.findViewById(R.id.ViewProfile);

        // Set username and profile image from database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("User Name").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    // Set username in TextView
                    if (textView != null) {
                        textView.setText(username != null ? username : "User");
                    }

                    // Set profile image using Glide
                    if (imageView != null) {
                        Glide.with(MainPage_FirstPage.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.baseline_person_24) // Default image if URL is null
                                .into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        // Open drawer on image button click
        imageButton.setOnClickListener(v -> drawerLayout.open());

        // Handle item clicks in NavigationView (Drawer)
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.item7) {
                showLogoutDialog();
                drawerLayout.closeDrawers();  // Close drawer after selection
                return true;
            }

            if (id == R.id.item3) {
                Intent intent = new Intent(MainPage_FirstPage.this, Blogs_Home.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }
            if (id == R.id.item4) {
                Intent intent = new Intent(MainPage_FirstPage.this, AIBot.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });

        // Handle BottomNavigationView item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item11) {
                if (fragmentManager != null) {
                    replaceFragment(new Home());
                }
                return true;
            } else if (id == R.id.item8) {
                Intent intent = new Intent(MainPage_FirstPage.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.item9) {
                showPopupMenu(findViewById(R.id.bottom_navigation));
                return true;
            } else if (id == R.id.item10) {
                Intent intent = new Intent(MainPage_FirstPage.this, Blogs_Home.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.item12) {
                if (fragmentManager != null) {
                    replaceFragment(new Profile());
                }
                return true;
            } else {
                return false;
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Sign Out", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SignPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish(); // Close current activity
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainPage_FirstPage.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popmenu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.option1) {
                Intent intent = new Intent(MainPage_FirstPage.this, HomeActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.option2) {
                Intent intent = new Intent(MainPage_FirstPage.this, Blog_Post.class);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void replaceFragment(Fragment fragment) {
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.framelayout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
