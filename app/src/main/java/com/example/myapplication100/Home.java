package com.example.myapplication100;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class Home extends Fragment {

    private boolean isLiked1 = false; // To track like status for Card 1
    private boolean isLiked2 = false; // For Card 2
    private boolean isLiked3 = false; // For Card 3

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Like ImageViews
        ImageView likeIcon1 = view.findViewById(R.id.like1);
        ImageView likeIcon2 = view.findViewById(R.id.like2);
        ImageView likeIcon3 = view.findViewById(R.id.like3);
        Button button1=view.findViewById(R.id.joinbutton1);
        Button button2=view.findViewById(R.id.joinbutton2);
        Button button3=view.findViewById(R.id.joinbutton3);
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(requireActivity(),HomeActivity.class);
        startActivity(intent);
    }
});
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(requireActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(requireActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });

        // Set up click listeners
        likeIcon1.setOnClickListener(v -> {
            isLiked1 = !isLiked1;
            toggleLike(likeIcon1, isLiked1);
        });

        likeIcon2.setOnClickListener(v -> {
            isLiked2 = !isLiked2;
            toggleLike(likeIcon2, isLiked2);
        });

        likeIcon3.setOnClickListener(v -> {
            isLiked3 = !isLiked3;
            toggleLike(likeIcon3, isLiked3);
        });

        return view;
    }

    // Utility function to toggle the like icon
    private void toggleLike(ImageView likeIcon, boolean isLiked) {
        if (isLiked) {
            likeIcon.setImageResource(R.drawable.baseline_thumb_up_alt_24);
        } else {
            likeIcon.setImageResource(R.drawable.baseline_thumb_up_24);
        }
    }
}
