package com.hasan.storyvibrance.Model;

public class PostModel {
    private String authorName;
    private String authorUsername;
    private String postTextContent;
    private int authorProfileImage;
    private int postMediaContent;
    private int likeCount;
    private int commentCount;

    public PostModel(String authorName, String authorUsername, String postTextContent, int authorProfileImage, int postMediaContent, int likeCount, int commentCount) {
        this.authorName = authorName;
        this.authorUsername = authorUsername;
        this.postTextContent = postTextContent;
        this.authorProfileImage = authorProfileImage;
        this.postMediaContent = postMediaContent;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getPostTextContent() {
        return postTextContent;
    }

    public void setPostTextContent(String postTextContent) {
        this.postTextContent = postTextContent;
    }

    public int getAuthorProfileImage() {
        return authorProfileImage;
    }

    public void setAuthorProfileImage(int authorProfileImage) {
        this.authorProfileImage = authorProfileImage;
    }

    public int getPostMediaContent() {
        return postMediaContent;
    }

    public void setPostMediaContent(int postMediaContent) {
        this.postMediaContent = postMediaContent;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
