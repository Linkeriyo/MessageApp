package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.messageapp.models.Message;
import com.example.messageapp.readapters.ContactsAdapter;
import com.example.messageapp.readapters.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private final static String TAG = "ChatActivity";
    String chatKey;
    RecyclerView recyclerView;
    List<Message> messageList = new ArrayList<>();
    DatabaseReference chatReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatKey = getIntent().getStringExtra("chatKey");
        loadMessages();
        setContentView(R.layout.activity_chat);
        setup();
    }

    public void setup() {
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MessageAdapter mAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(mAdapter);

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            TextView messageTextView = findViewById(R.id.messageTextView);
            chatReference.child(System.currentTimeMillis() + "-" + FirebaseAuth.getInstance().getUid())
                    .setValue(messageTextView.getText().toString());
            messageTextView.setText("");
        });
    }

    public void loadMessages() {
        chatReference = FirebaseDatabase.getInstance().getReference("chats/" + chatKey);
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                ArrayList<Message> messages = new ArrayList<>();
                snapshot.getChildren().forEach(child -> {
                    messages.add(new Message(child.getKey(), child.getValue(String.class)));
                });
                messageList.addAll(messages);
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}