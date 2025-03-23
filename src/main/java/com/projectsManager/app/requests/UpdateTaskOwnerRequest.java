package com.projectsManager.app.requests;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskOwnerRequest(
        @NotNull Long newOwnerId
) {
}
