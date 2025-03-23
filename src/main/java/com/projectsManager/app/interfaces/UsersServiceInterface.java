package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.UserNotFoundException;
import jakarta.validation.constraints.NotNull;

public interface UsersServiceInterface {
    User getById(Long id) throws UserNotFoundException;
}
