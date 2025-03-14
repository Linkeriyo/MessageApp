package com.example.messageapp.readapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messageapp.R;
import com.example.messageapp.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).sender.equals(FirebaseAuth.getInstance().getUid())) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent_row, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_received_row, parent, false);
        }

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.contentTextView.setText(messageList.get(position).content);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView contentTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            if (getItemViewType() == 0) {
                contentTextView = itemView.findViewById(R.id.sentMessageTextView);
            } else {
                contentTextView = itemView.findViewById(R.id.receivedMessageTextView);
            }
        }
    }

}
