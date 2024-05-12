package com.hasan.storyvibrance.Model;

public class PostSaveUserIdFireBase {
    private String userId;

    public PostSaveUserIdFireBase(String userId) {
        this.userId = userId;
    }

    public PostSaveUserIdFireBase() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
