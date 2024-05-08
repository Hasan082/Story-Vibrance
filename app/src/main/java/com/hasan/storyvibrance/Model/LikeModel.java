package com.hasan.storyvibrance.Model;

public class LikeModel {
    private String userId;

    public LikeModel() {

    }

    public LikeModel(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
