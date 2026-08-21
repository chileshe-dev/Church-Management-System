package com.isaiah.Church.Management.System.dto;

import com.isaiah.Church.Management.System.model.Role;

public class LoginResponse {

    private Integer userId;
    private String username;
    private Role role;

    public LoginResponse() {
    }

    public LoginResponse(Integer userId, String username, Role role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}