package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.Task;
import com.projectsManager.app.exceptions.TaskNotFoundException;

public interface TasksServiceInterface {
    Task getById(Long id) throws TaskNotFoundException;

    void update(Task task) throws TaskNotFoundException;
}
