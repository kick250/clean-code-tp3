package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.UserNotFoundException;

public interface UsersRepositoryInterface {
    User getById(Long id) throws UserNotFoundException;
}
