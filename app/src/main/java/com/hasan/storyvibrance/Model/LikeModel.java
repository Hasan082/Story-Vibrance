package com.hasan.storyvibrance.Model;

public class LikeModel {
    private String userId;
    private long timestamp;

    public LikeModel() {
    }

    public LikeModel(String userId, long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
