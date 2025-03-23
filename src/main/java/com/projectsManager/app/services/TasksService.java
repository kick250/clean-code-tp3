package com.projectsManager.app.services;

import com.projectsManager.app.domain.Task;
import com.projectsManager.app.exceptions.TaskNotFoundException;
import com.projectsManager.app.interfaces.TasksRepositoryInterface;
import com.projectsManager.app.interfaces.TasksServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class TasksService implements TasksServiceInterface {
    private final TasksRepositoryInterface tasksRepository;

    public TasksService(TasksRepositoryInterface tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Task getById(Long id) throws TaskNotFoundException {
        return tasksRepository.getById(id);
    }

    @Override
    public void update(Task task) throws TaskNotFoundException {
        tasksRepository.update(task);
    }
}
