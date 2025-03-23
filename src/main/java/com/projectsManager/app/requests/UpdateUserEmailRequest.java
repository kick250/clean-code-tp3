package com.projectsManager.app.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserEmailRequest(
        @Email
        @NotBlank
        String newEmail
) {
}
