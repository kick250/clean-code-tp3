package com.projectsManager.app.exceptions;

public class TaskNotFoundException extends Exception {
    public String getMessage() {
        return "Essa task não foi encontrada.";
    }
}
