package com.projectsManager.app.domain;

import com.projectsManager.app.enums.UserPosition;

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

    public void setAsSaved(long id) {
        this.id = id;
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
