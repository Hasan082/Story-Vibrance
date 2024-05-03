package com.hasan.storyvibrance.Model;

import java.util.List;

public class CommentModel {
    private String userId;
    private String commentText;
    private long timestamp;
    private List<ReplyModel> replies;

    public CommentModel() {
    }

    public CommentModel(String userId, String commentText, long timestamp, List<ReplyModel> replies) {
        this.userId = userId;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.replies = replies;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<ReplyModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyModel> replies) {
        this.replies = replies;
    }
}
