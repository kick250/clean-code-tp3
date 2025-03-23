package com.projectsManager.app.exceptions;

public class SprintNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Essa sprint não foi encontrada.";
    }
}
