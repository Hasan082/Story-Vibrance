package com.hasan.storyvibrance.Model;

public class PostSavedModel {

    private String userId;

    public PostSavedModel() {
    }

    public PostSavedModel(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
