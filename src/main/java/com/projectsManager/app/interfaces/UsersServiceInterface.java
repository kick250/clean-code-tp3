package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.UserNotFoundException;

public interface UsersServiceInterface {
    User getById(Long id) throws UserNotFoundException;

    void create(User user);

    void update(User user) throws UserNotFoundException;
}
