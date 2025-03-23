package com.projectsManager.app.domain;

import com.projectsManager.app.enums.TaskStatus;
import com.projectsManager.app.requests.CreateTaskRequest;

public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private User owner;
    private Sprint sprint;

    private boolean markedForDelete = false;

    public Task(Long id, String title, String description, TaskStatus status, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.owner = owner;
        this.sprint = null;
    }

    public Task(CreateTaskRequest request, User owner, Sprint sprint) {
        title = request.title();
        description = request.description();
        status = request.status();
        this.owner = owner;
        this.sprint = sprint;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setAsSaved(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    public String getOwnerEmail() {
        return owner.getEmail();
    }

    public long getSpringId() {
        return sprint.getId();
    }

    public boolean hasSprint() {
        return sprint != null;
    }

    public void markToDelete() {
        markedForDelete = true;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }
}
