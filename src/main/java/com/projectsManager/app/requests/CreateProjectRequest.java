package com.projectsManager.app.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(
        @NotBlank
        String name,
        @NotBlank
        String description
) {
}
