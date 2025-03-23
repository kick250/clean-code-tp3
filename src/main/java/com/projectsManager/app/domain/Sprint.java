package com.projectsManager.app.domain;

import com.projectsManager.app.exceptions.TaskNotFoundException;
import com.projectsManager.app.requests.CreateSprintRequest;
import com.projectsManager.app.requests.UpdateSprintRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sprint {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Project project;
    private List<Task> tasks;

    private boolean markedForDelete = false;

    public Sprint(Long id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = null;
        this.tasks = new ArrayList<>();
    }

    public Sprint(long id, LocalDate startDate, LocalDate endDate, List<Task> tasks) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = null;
        this.tasks = tasks;
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
        this.tasks = new ArrayList<>();
    }

    public void setAsSaved(long id) {
        this.id = id;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void addTask(Task task) {
        task.setSprint(this);
        tasks.add(task);
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

    public boolean hasProject() {
        return project != null;
    }

    public List<Task> listTasks() {
        return tasks;
    }

    public void markToDelete() {
        markedForDelete = true;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void removeTaskById(Long id) throws TaskNotFoundException {
        boolean found = false;

        for (Task task : tasks) {
            if (Objects.equals(task.getId(), id)) {
                task.markToDelete();
                found = true;
                break;
            }
        }

        if (!found) throw new TaskNotFoundException();
    }
}
