package com.example.week5day1firebasechat2.model;

public class Message {
    String key;
    String timeSent;
    String message;

    public Message() {
    }

    public Message(String key, String timeSent, String message) {
        this.key = key;
        this.timeSent = timeSent;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
