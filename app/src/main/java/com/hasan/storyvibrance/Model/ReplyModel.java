package com.hasan.storyvibrance.Model;

public class ReplyModel {
    private String userId;
    private String replyText;
    private String parentCommentId;
    private long timestamp;

    public ReplyModel() {
    }

    public ReplyModel(String userId, String replyText, String parentCommentId, long timestamp) {
        this.userId = userId;
        this.replyText = replyText;
        this.parentCommentId = parentCommentId;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
