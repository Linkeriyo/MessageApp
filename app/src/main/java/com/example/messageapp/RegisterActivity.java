package com.example.messageapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.messageapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.security.cert.Extension;
import java.util.function.ToDoubleBiFunction;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setup();
    }

    @SuppressLint("CheckResult")
    public void setup() {
        TextView registerEmailTextView = findViewById(R.id.registerEmailTextView);
        TextView registerNameTextView = findViewById(R.id.registerNameTextView);
        TextView registerNickTextView = findViewById(R.id.registerNickTextView);
        TextView registerPhotoUrlTextView = findViewById(R.id.registerPhotoUrlTextView);
        Button registerButton = findViewById(R.id.registerFinalButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            registerEmailTextView.setText(user.getEmail());
        }


        registerButton.setOnClickListener(v -> {
            if (registerEmailTextView.getText() != ""
                    && registerNameTextView.getText() != ""
                    && registerNickTextView.getText() != ""
                    && registerPhotoUrlTextView.getText() != "") {

                if (registerPhotoUrlTextView.getText().toString().matches("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)")) {
                    DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
                    usersReference.child(user.getUid()).setValue(new User(user.getUid(),
                            registerNameTextView.getText().toString(),
                            registerNickTextView.getText().toString(),
                            registerPhotoUrlTextView.getText().toString())
                    );
                    nextActivity();
                } else {
                    Toast.makeText(this,
                            "La URL no es válida.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                Toast.makeText(this,
                        "Todos los campos son obligatorios",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void nextActivity() {
        startActivity(new Intent(this, ContactsActivity.class));
        finish();
    }
}