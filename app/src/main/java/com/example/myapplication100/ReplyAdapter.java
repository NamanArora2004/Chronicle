package com.example.myapplication100;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private List<String> repliesList;

    public ReplyAdapter(List<String> repliesList) {
        this.repliesList = repliesList;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply_item, parent, false);
        return new ReplyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        String reply = repliesList.get(position);
        holder.replyText.setText(reply);
    }

    @Override
    public int getItemCount() {
        return repliesList.size();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        public TextView replyText;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyText = itemView.findViewById(R.id.et_reply_text);
        }
    }
}
