package com.example.demo.dto;


public class LoginResponse {
    private Integer userId;
    private String userType;

    public LoginResponse(Integer userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
