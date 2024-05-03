package com.hasan.storyvibrance.Model;

import java.util.List;

public class PostModel {
    private String authorName; // Name of the author
    private String authorUsername; // Username of the author
    private String postTextContent; // Content of the post
    private String authorImg; // URL or path of the author's profile image
    private String postMedia; // URL or path of the post media (image or video)
    private List<LikeModel> likes; // List of likes on the post
    private List<CommentModel> comments; // List of comments on the post
    private long timestamp; // Timestamp of when the post was created

    // Constructors, getters, and setters

    public PostModel() {
    }

    public PostModel(String authorName, String authorUsername, String postTextContent,
                     String authorImg, String postMedia, List<LikeModel> likes,
                     List<CommentModel> comments, long timestamp) {
        this.authorName = authorName;
        this.authorUsername = authorUsername;
        this.postTextContent = postTextContent;
        this.authorImg = authorImg;
        this.postMedia = postMedia;
        this.likes = likes;
        this.comments = comments;
        this.timestamp = timestamp;
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

    public String getAuthorImg() {
        return authorImg;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public String getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(String postMedia) {
        this.postMedia = postMedia;
    }

    public List<LikeModel> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeModel> likes) {
        this.likes = likes;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
