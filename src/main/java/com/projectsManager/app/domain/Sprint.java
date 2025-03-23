package com.projectsManager.app.domain;

import com.projectsManager.app.requests.CreateSprintRequest;
import com.projectsManager.app.requests.UpdateSprintRequest;

import java.time.LocalDate;

public class Sprint {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Project project;

    private boolean markedForDelete = false;

    public Sprint(Long id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Sprint(CreateSprintRequest request) {
        startDate = request.startDate();
        endDate = request.endDate();
    }

    public Sprint(Long id, UpdateSprintRequest request, Project project) {
        this.id = id;
        startDate = request.startDate();
        endDate = request.endDate();
        this.project = project;
    }

    public void setAsSaved(long id) {
        this.id = id;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getProjectId() {
        return project.getId();
    }

    public void markToDelete() {
        markedForDelete = true;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }
}
