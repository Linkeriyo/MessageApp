package com.example.messageapp.readapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messageapp.R;
import com.example.messageapp.models.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<User> userList;
    private Context context;
    private OnChatClickListener chatClickListener;

    public ContactsAdapter(List<User> userList, Context context, OnChatClickListener chatClickListener) {
        this.userList = userList;
        this.context = context;
        this.chatClickListener = chatClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ContactViewHolder(v, chatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User user = userList.get(position);

        Glide.with(context).load(user.getImage()).into(holder.profilePicView);
        holder.nickView.setText(user.getNick());
        holder.nameView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircularImageView profilePicView;
        private final TextView nameView;
        private TextView nickView;
        private OnChatClickListener chatClickListener;

        public ContactViewHolder(@NonNull View itemView, OnChatClickListener chatClickListener) {
            super(itemView);
            this.chatClickListener = chatClickListener;
            profilePicView = itemView.findViewById(R.id.profilePicView);
            nameView = itemView.findViewById(R.id.nameView);
            nickView = itemView.findViewById(R.id.nickView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            chatClickListener.onChatClick(getAdapterPosition());
        }
    }

    public interface OnChatClickListener {
        void onChatClick(int position);
    }
}
