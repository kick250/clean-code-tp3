package com.projectsManager.app.domain;

import com.projectsManager.app.requests.CreateProjectRequest;
import com.projectsManager.app.requests.UpdateProjectRequest;
import jakarta.validation.Valid;

public class Project {
    private Long id;
    private String name;
    private String description;

    public Project(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Project(CreateProjectRequest request) {
        name = request.name();
        description = request.description();
    }

    public Project(Long id, UpdateProjectRequest request) {
        this.id = id;
        name = request.name();
        description = request.description();
    }

    public void setAsSeved(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
