package com.sismics.docs.core.dao.dto;

public class UserRequestDto {
    private String name;
    private String email;

    public UserRequestDto() {}

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
}
