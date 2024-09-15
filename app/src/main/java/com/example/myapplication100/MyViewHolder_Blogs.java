package com.example.myapplication100;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_Blogs extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textViewTitle;
    TextView textViewAuthor;

    public MyViewHolder_Blogs(@NonNull View itemView) {
        super(itemView);

        // Initialize the views
        imageView = itemView.findViewById(R.id.imageBlog);
        textViewTitle = itemView.findViewById(R.id.textview_title);
        textViewAuthor = itemView.findViewById(R.id.textview_author);
    }

    // Getter methods for views
    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextViewTitle() {
        return textViewTitle;
    }

    public TextView getTextViewAuthor() {
        return textViewAuthor;
    }


}
