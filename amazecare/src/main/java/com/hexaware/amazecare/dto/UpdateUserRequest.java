package com.hexaware.amazecare.dto;

import com.hexaware.amazecare.entity.Status;

public class UpdateUserRequest {
    private String name;
    private String email;
    private Status status;

    public UpdateUserRequest() {}

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
