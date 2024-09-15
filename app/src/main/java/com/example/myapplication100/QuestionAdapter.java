package com.example.myapplication100;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;
    private OnItemClickListener onItemClickListener;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    public interface OnItemClickListener {
        void onReplyClick(int position);  // Handle reply click
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.questionText.setText(question.getQuestionText());

        // Initialize with the + icon and hidden reply menu
        holder.replyMenu.setVisibility(View.GONE);
        holder.toggleIcon.setImageResource(R.drawable.baseline_add_24);

        // Convert replies from Map to List and set up the adapter
        List<String> replyList = new ArrayList<>();
        Map<String, String> repliesMap = question.getReplies(); // Assuming getReplies() returns Map<String, String>
        if (repliesMap != null) {
            replyList.addAll(repliesMap.values());
        }

        holder.replyListAdapter = new ReplyListAdapter(replyList);
        holder.replyList.setAdapter(holder.replyListAdapter);

        // Set up click listeners for the toggle (+/-) functionality
        holder.toggleIcon.setOnClickListener(v -> {
            if (holder.replyMenu.getVisibility() == View.GONE) {
                holder.replyMenu.setVisibility(View.VISIBLE);  // Show the reply menu
                holder.toggleIcon.setImageResource(R.drawable.baseline_minimize_24);  // Change to "-" icon
            } else {
                holder.replyMenu.setVisibility(View.GONE);  // Hide the reply menu
                holder.toggleIcon.setImageResource(R.drawable.baseline_add_24);  // Change to "+" icon
            }
        });

        // Handle reply button click
        holder.replyIcon.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onReplyClick(position);  // Trigger the reply action
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    // ViewHolder class
    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        public TextView questionText;
        public ImageView toggleIcon;  // For + and - button
        public View replyMenu;  // Pop-up menu for replying
        public RecyclerView replyList;  // RecyclerView to show replies
        public Button replyIcon;  // Reply button inside the pop-up menu
        public ReplyListAdapter replyListAdapter;

        public QuestionViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            toggleIcon = itemView.findViewById(R.id.toggle_button);  // Toggle button for +/-
            replyMenu = itemView.findViewById(R.id.reply_menu);  // Hidden menu for replies
            replyList = replyMenu.findViewById(R.id.reply_list);  // RecyclerView for replies
            replyIcon = replyMenu.findViewById(R.id.reply_button);  // Reply button inside the menu

            // Initialize RecyclerView for replies
            replyList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
