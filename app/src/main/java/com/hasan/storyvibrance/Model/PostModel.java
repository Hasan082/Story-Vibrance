package com.hasan.storyvibrance.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostModel {
    private String postId; //Post Id
    private String authorName; // Name of the author
    private String authorUsername; // Username of the author
    private String postTextContent; // Content of the post
    private String authorImg; // URL or path of the author's profile image
    private String postMedia; // URL or path of the post media (image or video)
    private List<LikeModel> likes; // List of likes on the post
    private List<CommentModel> comments; // List of comments on the post
    private String timestamp; // Timestamp of when the post was created
    private boolean saved; //Save post

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

    /**
     * Adds a like to the post.
     * @param like The LikeModel object representing the like to be added.
     */
    public void addLike(LikeModel like) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(like);
    }

    /**
     * Removes a like from the post.
     * @param userId The ID of the user whose like is to be removed.
     */
    public void removeLike(String userId) {
        if (likes != null) {
            for (Iterator<LikeModel> iterator = likes.iterator(); iterator.hasNext();) {
                LikeModel like = iterator.next();
                if (like.getUserId().equals(userId)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * Checks if the post is liked by a specific user.
     * @param userId The ID of the user to check for liking the post.
     * @return True if the post is liked by the specified user, otherwise false.
     */
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

    /**
     * Gets the total number of likes on the post.
     * @return The total number of likes on the post.
     */
    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }


    public boolean isSaved() {
        return saved;
    }


    public void setSaved(boolean saved) {
        this.saved = saved;
    }
    /**
     * Checks if the post is saved.
     * @return true if the post is saved, false otherwise.
     */
    public boolean isSaved(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts", Context.MODE_PRIVATE);
        String savedAsString = sharedPreferences.getString(postId, "false");
        return Boolean.parseBoolean(savedAsString);
    }


    /**
     * Sets the saved status of the post.
     * @param saved true if the post should be saved, false otherwise.
     */
    public void setSaved(Context context, boolean saved) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(postId, String.valueOf(saved));
        editor.apply();
        this.saved = saved;
    }


}
