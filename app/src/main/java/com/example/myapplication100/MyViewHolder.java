package com.example.myapplication100;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    ImageView imageView2;
    TextView textView;
    TextView textView2;
    TextView textView3;
    Button button;

    ImageView like;
    ImageView comment;
    ImageView qna;
View view;


    //    TextView textView4;
    boolean isColored = false;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        like=itemView.findViewById(R.id.like);
        qna=itemView.findViewById(R.id.qna);
        imageView=itemView.findViewById(R.id.imagemmunity);
        imageView2=itemView.findViewById(R.id.image_shop);
        textView=itemView.findViewById(R.id.tv1);
        textView2=itemView.findViewById(R.id.tv2);
        textView3=itemView.findViewById(R.id.tv3);
        button=itemView.findViewById(R.id.follow);
        // Declare this at the top of your class
view=itemView;
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isColored) {
                    // Change to black color
                    like.setColorFilter(v.getContext().getResources().getColor(android.R.color.black));
                } else {
                    // Change to white color
                    like.setColorFilter(v.getContext().getResources().getColor(android.R.color.white));
                }
                isColored = !isColored;
            }
        });


//        textView4=itemView.findViewById(R.id.tv4);
    }
}
