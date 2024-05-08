package com.hasan.storyvibrance.Model;

public class UserDataModel {
    private String fullName;
    private String profileImg;

    public UserDataModel(String fullName, String profileImg) {
        this.fullName = fullName;
        this.profileImg = profileImg;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
