package com.projectsManager.app.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequest(
        @NotBlank
        String name,
        @NotBlank
        String description
) {
}
