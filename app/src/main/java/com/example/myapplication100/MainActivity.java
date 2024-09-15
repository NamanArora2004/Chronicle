package com.example.myapplication100;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private static final int REQUEST_CODE_SHOP_IMAGE = 102;
    private ImageView imageDp, imageShop;
    private EditText inputName, description, shopName;
    private Button btnAdddp;
    private ProgressBar progressBar1, progressBar2;
    private TextView progressTextview1, progressTextview2;

    private Uri imageUri, shopImageUri;
    private boolean isImageAdded = false, isShopImageAdded = false;

    private DatabaseReference dataRef;
    private StorageReference storageRefDp, storageRefShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        imageDp = findViewById(R.id.image);
        imageShop = findViewById(R.id.image2);
        inputName = findViewById(R.id.edittext1);
        description = findViewById(R.id.edittext2);
        shopName = findViewById(R.id.edittext3);
        progressBar1 = findViewById(R.id.progress_bar);
        progressBar2 = findViewById(R.id.progress_bar2);
        btnAdddp = findViewById(R.id.button);
        progressTextview1 = findViewById(R.id.tviewProgress);
        progressTextview2 = findViewById(R.id.tviewProgress2);

        // Set initial visibility of progress bars
        progressBar1.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        progressTextview1.setVisibility(View.GONE);
        progressTextview2.setVisibility(View.GONE);

        // Firebase references
        dataRef = FirebaseDatabase.getInstance().getReference().child("Community");
        storageRefDp = FirebaseStorage.getInstance().getReference().child("CommunityDpImages");
        storageRefShop = FirebaseStorage.getInstance().getReference().child("CommunityShopImages");

        // Select DP image
        imageDp.setOnClickListener(v -> selectImage(REQUEST_CODE_IMAGE));

        // Select shop image
        imageShop.setOnClickListener(v -> selectImage(REQUEST_CODE_SHOP_IMAGE));

        // Add community
        btnAdddp.setOnClickListener(v -> {
            final String imageName = inputName.getText().toString();
            final String shopNameValue = shopName.getText().toString();
            final String descriptionValue = description.getText().toString();

            if (isValid(imageName, shopNameValue, descriptionValue)) {
                uploadImages(imageName, shopNameValue, descriptionValue);
            } else {
                Toast.makeText(MainActivity.this, "Please select images and fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to select image from gallery
    private void selectImage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    // Validation for inputs
    private boolean isValid(String imageName, String shopNameValue, String descriptionValue) {
        return isImageAdded && isShopImageAdded && !imageName.isEmpty() && !shopNameValue.isEmpty() && !descriptionValue.isEmpty();
    }

    // Upload DP and Shop images
    private void uploadImages(final String imageName, final String shopNameValue, final String descriptionValue) {
        progressTextview1.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        final String key = dataRef.push().getKey();

        // Upload DP image to Firebase
        storageRefDp.child(key + "_dp.jpg").putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageRefDp.child(key + "_dp.jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                    final String dpUrl = uri.toString();

                    // Upload Shop image
                    storageRefShop.child(key + "_shop.jpg").putFile(shopImageUri).addOnSuccessListener(taskSnapshot1 ->
                            storageRefShop.child(key + "_shop.jpg").getDownloadUrl().addOnSuccessListener(shopUri -> {
                                final String shopUrl = shopUri.toString();

                                // Save community data to Firebase
                                saveToDatabase(imageName, descriptionValue, dpUrl, shopUrl, shopNameValue);
                            })
                    );
                })
        );
    }

    // Function to save community details to Firebase
    private void saveToDatabase(String imageName, String description, String dpUrl, String shopImageUrl, String shopName) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "Please log in to create a community", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        // Data map
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("CommunityName", imageName);
        hashMap.put("Description", description);
        hashMap.put("ImageUrl", dpUrl);
        hashMap.put("ShopImageUrl", shopImageUrl);
        hashMap.put("ShopName", shopName);
        hashMap.put("UserId", userId);  // Store creator's user ID

        // Add data to Firebase database
        dataRef.child(dataRef.push().getKey()).setValue(hashMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(MainActivity.this, "Community Successfully Created", Toast.LENGTH_SHORT).show();
            progressBar1.setVisibility(View.GONE);
            progressTextview1.setVisibility(View.GONE);
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        });
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null && data.getData() != null) {
            imageUri = data.getData();
            isImageAdded = true;
            imageDp.setImageURI(imageUri);
        } else if (requestCode == REQUEST_CODE_SHOP_IMAGE && data != null && data.getData() != null) {
            shopImageUri = data.getData();
            isShopImageAdded = true;
            imageShop.setImageURI(shopImageUri);
        }
    }
}
