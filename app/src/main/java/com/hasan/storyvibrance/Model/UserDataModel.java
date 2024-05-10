package com.hasan.storyvibrance.Model;

public class UserDataModel {
    private String profileImgUrl;
    private String email;
    private  String name;

    public UserDataModel() {
    }

    public UserDataModel(String email, String name, String profileImgUrl) {
        this.email = email;
        this.name = name;
        this.profileImgUrl = profileImgUrl;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
