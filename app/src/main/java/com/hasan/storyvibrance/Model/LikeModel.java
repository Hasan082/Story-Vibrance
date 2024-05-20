package com.hasan.storyvibrance.Model;

import java.util.HashMap;
import java.util.Map;

public class LikeModel {
    private String userId;

    public LikeModel() {

    }

    public LikeModel(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return map;
    }
}
