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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Blog_Post extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView blogImage;
    private EditText blogContent, authorName, blogTitle;  // Added blogTitle
    private Button btnUpload;
    private ProgressBar progressBar;
    private TextView progressTextView;

    Uri blogImageUri;
    boolean isBlogImageAdded = false;

    DatabaseReference blogDataRef;
    StorageReference storageRefBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);

        // Initialize views
        blogImage = findViewById(R.id.imageBlog);
        blogContent = findViewById(R.id.textview2);
        authorName = findViewById(R.id.authorName);
        blogTitle = findViewById(R.id.blogTitle); // Added blogTitle initialization
        progressBar = findViewById(R.id.progress_bar_blog); // Updated progress bar ID
        btnUpload = findViewById(R.id.btn1);
        progressTextView = findViewById(R.id.tviewProgressBlog);

        progressBar.setVisibility(View.GONE);
        progressTextView.setVisibility(View.GONE);

        blogDataRef = FirebaseDatabase.getInstance().getReference().child("Blogs");
        storageRefBlog = FirebaseStorage.getInstance().getReference().child("BlogImages");

        // Image selection for blog post
        blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // Upload button logic
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String blogContentValue = blogContent.getText().toString();
                final String authorNameValue = authorName.getText().toString();
                final String blogTitleValue = blogTitle.getText().toString();  // Get the blog title

                if (isValid(blogContentValue, authorNameValue, blogTitleValue)) {
                    uploadBlog(blogContentValue, blogTitleValue, authorNameValue);
                } else {
                    Toast.makeText(Blog_Post.this, "Please select an image and fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    // Modified validation method to include blogTitle
    private boolean isValid(String blogContent, String authorName, String blogTitle) {
        return isBlogImageAdded && !blogContent.isEmpty() && !authorName.isEmpty() && !blogTitle.isEmpty();
    }

    private void uploadBlog(final String blogContentValue, final String blogTitleValue, final String authorNameValue) {
        progressTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final String key = blogDataRef.push().getKey();

        storageRefBlog.child(key + "_blog.jpg").putFile(blogImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRefBlog.child(key + "_blog.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String blogImageUrl = uri.toString();
                        saveToDatabase(blogContentValue, blogTitleValue, blogImageUrl, authorNameValue);
                    }
                });
            }
        });
    }

    // Modified saveToDatabase method to include blogTitle
    private void saveToDatabase(String blogContent, String blogTitle, String blogImageUrl, String authorName) {
        HashMap<String, String> blogMap = new HashMap<>();
        blogMap.put("Content", blogContent);
        blogMap.put("BlogImageUrl", blogImageUrl);
        blogMap.put("AuthorName", authorName);
        blogMap.put("BlogTitle", blogTitle);  // Save the blog title

        blogDataRef.child(blogDataRef.push().getKey()).setValue(blogMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Blog_Post.this, "Blog Successfully Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                startActivity(new Intent(Blog_Post.this,Blogs_Home .class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null && data.getData() != null) {
            blogImageUri = data.getData();
            isBlogImageAdded = true;
            blogImage.setImageURI(blogImageUri);
        }
    }
}
