package com.projectsManager.app.responses;

import com.projectsManager.app.domain.Project;

public record ProjectResponse(Long id, String name, String description) {
    public ProjectResponse(Project project) {
        this(project.getId(), project.getName(), project.getDescription());
    }
}
