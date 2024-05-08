package com.hasan.storyvibrance.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostModel {
    private String postId;
    private String authorName; // Name of the author
    private String authorUsername; // Username of the author
    private String postTextContent; // Content of the post
    private String authorImg; // URL or path of the author's profile image
    private String postMedia; // URL or path of the post media (image or video)
    private List<LikeModel> likes; // List of likes on the post
    private List<CommentModel> comments; // List of comments on the post
    private String timestamp; // Timestamp of when the post was created

    // Constructors, getters, and setters

    public PostModel() {
    }

    public PostModel(String postId,String authorName, String authorUsername, String postTextContent,
                     String authorImg, String postMedia, List<LikeModel> likes,
                     List<CommentModel> comments, String timestamp) {
        this.postId=postId;
        this.authorName = authorName;
        this.authorUsername = authorUsername;
        this.postTextContent = postTextContent;
        this.authorImg = authorImg;
        this.postMedia = postMedia;
        this.likes = likes;
        this.comments = comments;
        this.timestamp = timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Method to add a like to the post
    public void addLike(LikeModel like) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(like);
    }

    // Method to remove a like from the post
    public void removeLike(String userId) {
        if (likes != null) {
            for (Iterator<LikeModel> iterator = likes.iterator(); iterator.hasNext();) {
                LikeModel like = iterator.next();
                if (like.getUserId().equals(userId)) {
                    iterator.remove();
                    break; // Assuming each user can like a post only once, so no need to continue iterating
                }
            }
        }
    }

    // Method to check if a user liked the post
    public boolean isLikedByUser(String userId) {
        if (likes != null) {
            for (LikeModel like : likes) {
                if (like.getUserId().equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to get the total like count
    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }

}
