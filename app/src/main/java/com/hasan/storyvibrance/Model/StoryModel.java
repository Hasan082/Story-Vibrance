package com.hasan.storyvibrance.Model;

public class StoryModel {
    private String username;
    private int profileIconResId;
    private int storyImageResId;

    public StoryModel(String username, int profileIconResId, int storyImageResId) {
        this.username = username;
        this.profileIconResId = profileIconResId;
        this.storyImageResId = storyImageResId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProfileIconResId() {
        return profileIconResId;
    }

    public void setProfileIconResId(int profileIconResId) {
        this.profileIconResId = profileIconResId;
    }

    public int getStoryImageResId() {
        return storyImageResId;
    }

    public void setStoryImageResId(int storyImageResId) {
        this.storyImageResId = storyImageResId;
    }
}
