package com.projectsManager.app.domain;

import com.projectsManager.app.enums.UserPosition;
import com.projectsManager.app.requests.CreateUserRequest;

public class User {
    private Long id;
    private String name;
    private String email;
    private UserPosition position;

    public User(long id, String name, String email, UserPosition position) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
    }

    public User(CreateUserRequest request) {
        name = request.name();
        email = request.email();
        position = request.position();
    }

    public void setAsSaved(long id) {
        this.id = id;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void setPosition(UserPosition position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserPosition getPosition() {
        return position;
    }
}
