package com.example.myapplication100;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Profile extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView profileName, profileEmail, profilePhoneNumber;
    private ImageView profileImage;
    private Button btnEditProfile, btnLogout, btnUploadImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize FirebaseAuth and FirebaseStorage
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("profile_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Get the current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
//        profilePhoneNumber = view.findViewById(R.id.profile_phone_number);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnUploadImage = view.findViewById(R.id.btn_upload_image); // Add a button to upload image

        if (currentUser != null) {
            // Fetch user data from Firebase Realtime Database
            String userId = currentUser.getUid();
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("User Name").getValue(String.class);
                        String email = dataSnapshot.child("Email").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("Phone Number").getValue(String.class);
                        String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);

                        profileName.setText(username);
                        profileEmail.setText(email);
//                        profilePhoneNumber.setText(phoneNumber);

                        // Load profile image from URL using Picasso
                        Picasso.get()
                                .load(profileImageUrl)
                                .placeholder(R.drawable.baseline_person_24) // Default image if URL is null
                                .into(profileImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors
                }
            });
        } else {
            profileName.setText("Guest");
            profileEmail.setText("guest@example.com");
            profilePhoneNumber.setText("N/A");
        }

        // Handle image selection from gallery
        profileImage.setOnClickListener(v -> openFileChooser());

        // Handle image upload to Firebase
        btnUploadImage.setOnClickListener(v -> uploadImageToFirebase());

        // Handle Edit Profile button
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Handle Logout button
        btnLogout.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getActivity(), SignPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // Open file chooser to select an image from gallery
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.baseline_person_24) // Default image if URL is null
                    .into(profileImage); // Display selected image
        }
    }

    // Upload the selected image to Firebase Storage
    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the image URL to Firebase Database
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            databaseReference.child(userId).child("profileImageUrl").setValue(uri.toString());
                            Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Get the file extension for the image
    private String getFileExtension(Uri uri) {
        String extension = null;

        // Get the file extension using MimeTypeMap and the URI scheme
        if (getActivity() != null && getActivity().getContentResolver() != null) {
            String mimeType = getActivity().getContentResolver().getType(uri);
            if (mimeType != null) {
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            }
        }

        // If mimeType is null or there's no extension found, try extracting from URI directly
        if (extension == null) {
            String path = uri.getPath();
            if (path != null) {
                int dotIndex = path.lastIndexOf('.');
                if (dotIndex > -1) {
                    extension = path.substring(dotIndex + 1);
                }
            }
        }

        return extension;
    }
}
