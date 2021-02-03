package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.messageapp.models.User;
import com.example.messageapp.readapters.ContactsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.OnChatClickListener {

    private static final String TAG = "ContactsActivity";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setContentView(R.layout.activity_contacts);
        setup();
    }

    private void loadData() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppData.userList.clear();
                ArrayList<User> users = new ArrayList<>();
                snapshot.getChildren().forEach(child -> {
                    User user = child.getValue(User.class);
                    if (!user.userId.equals(FirebaseAuth.getInstance().getUid())) {
                        users.add(user);
                    }
                });
                AppData.userList.addAll(users);
                updateRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setup() {
        recyclerView = findViewById(R.id.contactRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContactsAdapter mAdapter = new ContactsAdapter(AppData.userList, this, this);
        recyclerView.setAdapter(mAdapter);
        if (!AppData.userList.isEmpty()) {
            findViewById(R.id.noContactsTextView).setVisibility(View.INVISIBLE);
        }

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onChatClick(int position) {
        Log.d(TAG, AppData.userList.get(position).userId);
        String chatKey = null;
        for (String key : AppData.chatKeyList) {
            if (key.contains(AppData.userList.get(position).userId)) {
                chatKey = key;
            }
        }

        if (chatKey == null) {
            chatKey = FirebaseAuth.getInstance().getUid() + "-" + AppData.userList.get(position).userId;
        }

        startActivity(new Intent(this, ChatActivity.class).putExtra("chatKey", chatKey));
    }

    public void updateRecyclerView() {
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.invalidate();
        if (!AppData.userList.isEmpty()) {
            findViewById(R.id.noContactsTextView).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.noContactsTextView).setVisibility(View.VISIBLE);
        }
    }
}