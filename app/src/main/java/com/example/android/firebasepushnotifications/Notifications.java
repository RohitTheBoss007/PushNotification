package com.example.android.firebasepushnotifications;

public class Notifications {
    String from,message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notifications(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public Notifications() {
    }
}
