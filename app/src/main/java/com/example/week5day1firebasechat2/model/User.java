package com.example.week5day1firebasechat2.model;

public class User {
    String eMail;
    String passWord;
    String key;
    String timeSent;
    String message;

    public User() {
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

    public User(String eMail, String passWord, String key, String timeSent, String message) {
        this.eMail = eMail;
        this.passWord = passWord;
        this.key = key;
        this.timeSent = timeSent;
        this.message = message;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
