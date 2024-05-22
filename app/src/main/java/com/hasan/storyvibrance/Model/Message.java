package com.hasan.storyvibrance.Model;

public class Message {
    private String content;
    private long timestamp;

    private String senderUserId;

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Message(String content, String senderUserId, long timestamp) {
        this.content = content;
        this.senderUserId = senderUserId;
        this.timestamp = timestamp;
    }

    public Message() {
        // Default constructor required for Firestore
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
