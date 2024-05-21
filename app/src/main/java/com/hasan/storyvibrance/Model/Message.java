package com.hasan.storyvibrance.Model;

public class Message {
    private String content;
    private long timestamp; // Store timestamp as long

    public Message() {
        // Default constructor required for Firestore
    }

    public Message(String content, long timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
