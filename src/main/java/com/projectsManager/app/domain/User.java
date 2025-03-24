package com.projectsManager.app.domain;

import com.projectsManager.app.enums.UserPosition;
import com.projectsManager.app.requests.CreateUserRequest;

public class User {
    private final Long id;
    private final String name;
    private final String email;
    private final UserPosition position;

    public User(long id, String name, String email, UserPosition position) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
    }

    public User(CreateUserRequest request) {
        id = null;
        name = request.name();
        email = request.email();
        position = request.position();
    }

    public User setAsSaved(long id) {
        return new User(id, name, email, position);
    }

    public User updateEmail(String email) {
        return new User(id, name, email, position);
    }

    public User setPosition(UserPosition position) {
        return new User(id, name, email, position);
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
