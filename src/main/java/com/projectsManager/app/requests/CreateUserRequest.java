package com.projectsManager.app.requests;

import com.projectsManager.app.enums.UserPosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email,
        @NotNull
        UserPosition position
) {
}
