package com.hasan.storyvibrance.Model;

public class PostSavedModel {

    private  String postId;
    private String postContent;
    private String postMedia;

    public PostSavedModel() {
    }

    public PostSavedModel(String postId, String postContent, String postMedia) {
        this.postId = postId;
        this.postContent = postContent;
        this.postMedia = postMedia;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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


}
