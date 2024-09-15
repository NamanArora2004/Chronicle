package com.example.myapplication100;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.ReplyViewHolder> {

    private List<String> replies;

    public ReplyListAdapter(List<String> replies) {
        this.replies = replies;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        String reply = replies.get(position);
        holder.replyText.setText(reply);  // This line is causing NullPointerException
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    // ViewHolder class
    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        public TextView replyText;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyText = itemView.findViewById(R.id.reply_text);  // Ensure this ID matches the one in item_reply.xml
        }
    }
}
