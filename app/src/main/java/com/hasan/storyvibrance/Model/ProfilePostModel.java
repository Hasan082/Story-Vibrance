package com.hasan.storyvibrance.Model;

public class ProfilePostModel {

    private String postId;
    private String profilePostMedia;

    public ProfilePostModel() {
    }

    public ProfilePostModel(String profilePostMedia) {
        this.profilePostMedia = profilePostMedia;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getProfilePostMedia() {
        return profilePostMedia;
    }

    public void setProfilePostMedia(String profilePostMedia) {
        this.profilePostMedia = profilePostMedia;
    }
}
