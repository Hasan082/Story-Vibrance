package com.hasan.storyvibrance.Model;

public class FriendSearchModel {
    private String id;
    private String name;
    private String email;
    private String ProfileImg;
    private String status;

    public FriendSearchModel() {
    }

    public FriendSearchModel(String id, String name, String email, String profileImg, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        ProfileImg = profileImg;
        this.status = status;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }
}
