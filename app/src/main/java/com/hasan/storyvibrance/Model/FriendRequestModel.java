package com.hasan.storyvibrance.Model;

public class FriendRequestModel {
    private String requestId;
    private String recipientId;
    private String senderId;
    private String senderName;
    private String senderProfileImageUrl;
    private Long timestamp;


    // Constructors, getters, and setters

    public FriendRequestModel() {

    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public FriendRequestModel(String recipientId, String senderId, String senderName, String senderProfileImageUrl, Long timestamp) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.timestamp = timestamp;

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String requestId) {
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    public void setSenderProfileImageUrl(String senderProfileImageUrl) {
        this.senderProfileImageUrl = senderProfileImageUrl;
    }
}
