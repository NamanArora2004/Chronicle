package com.example.myapplication100;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

// ViewHolder class
public  class QuestionViewHolder extends RecyclerView.ViewHolder {

    public TextView questionText;
    public ImageView toggleIcon; // For + and - button
    public View replyMenu; // The pop-up menu for replies

    public QuestionViewHolder(@NonNull View itemView, QuestionAdapter.OnItemClickListener listener) {
        super(itemView);
        questionText = itemView.findViewById(R.id.question_text);
        toggleIcon = itemView.findViewById(R.id.toggle_button); // This will act as the + or - button
        replyMenu = itemView.findViewById(R.id.reply_menu); // This is the hidden pop-up menu for replying

        replyMenu.setVisibility(View.GONE); // Initially, hide the pop-up menu

        // Set up the toggle button for + and - functionality
        toggleIcon.setOnClickListener(v -> {
            if (replyMenu.getVisibility() == View.GONE) {
                replyMenu.setVisibility(View.VISIBLE); // Show the menu
                toggleIcon.setImageResource(R.drawable.baseline_minimize_24); // Change to "-" icon
            } else {
                replyMenu.setVisibility(View.GONE); // Hide the menu
                toggleIcon.setImageResource(R.drawable.baseline_add_24); // Change to "+" icon
            }
        });
    }
}
