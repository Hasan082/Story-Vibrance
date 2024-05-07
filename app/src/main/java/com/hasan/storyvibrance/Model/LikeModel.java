package com.hasan.storyvibrance.Model;

public class LikeModel {
    private String userId;
    private String postId;

    public LikeModel() {

    }

    public LikeModel(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
