package com.hasan.storyvibrance.Model;

public class FriendRequestModel {
    private String requestId;
    private String senderId;
    private String senderName;
    private String senderProfileImageUrl;
    private long requestSendTime;


    // Constructors, getters, and setters

    public FriendRequestModel(long requestSendTime) {

    }

    public long getRequestSendTime() {
        return requestSendTime;
    }

    public void setRequestSendTime(long requestSendTime) {
        this.requestSendTime = requestSendTime;
    }

    public FriendRequestModel(String requestId, String senderId, String senderName, String senderProfileImageUrl, long requestSendTime) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.requestSendTime = requestSendTime;

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
