package com.projectsManager.app.exceptions;

public class ProjectNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Esse projeto não foi encontrado.";
    }
}
