package com.projectsManager.app.exceptions;

public class SaveErrorException extends RuntimeException {
    public String getMessage() {
        return "Ocorreu um erro a salvar objeto no banco.";
    }
}
