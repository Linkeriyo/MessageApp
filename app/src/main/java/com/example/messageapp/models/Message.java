package com.example.messageapp.models;

public class Message {

    public String sender;
    public Long timeMillis;
    public String content;

    public Message(String key, String value) {
        sender = key.split("-")[1];
        timeMillis = Long.valueOf(key.split("-")[0]);
        content = value;
    }

    public String getKey() {
        return timeMillis  + "-" + sender;
    }

    public String getValue() {
        return content;
    }
}
