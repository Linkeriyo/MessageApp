package com.example.messageapp.models;

import java.io.Serializable;

public class User {

    public String name, nick, image, userId;

    public User() {

    }

    public User(String userId, String name, String nick, String image) {
        this.userId = userId;
        this.name = name;
        this.nick = nick;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getImage() {
        return image;
    }

    public String getUserId() {
        return userId;
    }
}
