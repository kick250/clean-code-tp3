package com.projectsManager.app.services;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.UserNotFoundException;
import com.projectsManager.app.interfaces.UsersRepositoryInterface;
import com.projectsManager.app.interfaces.UsersServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UsersServiceInterface {
    private final UsersRepositoryInterface usersRepository;

    public UsersService(UsersRepositoryInterface usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        return usersRepository.getById(id);
    }
}
