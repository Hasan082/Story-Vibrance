package com.hasan.storyvibrance.Model;

public class PostSavedModel {

    private String userId;
    private String postContent;
    private String postMedia;

    public PostSavedModel() {
    }

    public PostSavedModel(String userId, String postContent, String postMedia) {
        this.userId = userId;
        this.postContent = postContent;
        this.postMedia = postMedia;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(String postMedia) {
        this.postMedia = postMedia;
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
