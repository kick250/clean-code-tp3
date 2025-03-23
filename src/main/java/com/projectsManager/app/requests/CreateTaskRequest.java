package com.projectsManager.app.requests;

import com.projectsManager.app.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        TaskStatus status,
        @NotNull
        Long ownerId
) {
}
