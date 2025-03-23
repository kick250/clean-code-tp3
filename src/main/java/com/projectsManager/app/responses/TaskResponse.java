package com.projectsManager.app.responses;

import com.projectsManager.app.domain.Task;
import com.projectsManager.app.enums.TaskStatus;

public record TaskResponse(Long id, String title, String description, TaskStatus status, Long ownerId, String ownerEmail) {
    public TaskResponse(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getOwnerId(), task.getOwnerEmail());
    }
}
