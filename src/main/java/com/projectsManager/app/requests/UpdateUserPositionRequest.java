package com.projectsManager.app.requests;

import com.projectsManager.app.enums.UserPosition;
import jakarta.validation.constraints.NotNull;

public record UpdateUserPositionRequest(
        @NotNull
        UserPosition newPosition
) {
}
