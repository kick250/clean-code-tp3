package com.projectsManager.app.requests;

import com.projectsManager.app.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull
        TaskStatus newTaskStatus
) {
}
