package com.projectsManager.app.exceptions;

public class UserNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Esse usuário não foi encontrado.";
    }
}
