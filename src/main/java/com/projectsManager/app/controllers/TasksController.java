package com.projectsManager.app.controllers;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.domain.Task;
import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.SprintNotFoundException;
import com.projectsManager.app.exceptions.TaskNotFoundException;
import com.projectsManager.app.exceptions.UserNotFoundException;
import com.projectsManager.app.interfaces.SprintsServiceInterface;
import com.projectsManager.app.interfaces.TasksServiceInterface;
import com.projectsManager.app.interfaces.UsersServiceInterface;
import com.projectsManager.app.repositories.UsersRepository;
import com.projectsManager.app.requests.CreateTaskRequest;
import com.projectsManager.app.requests.UpdateTaskOwnerRequest;
import com.projectsManager.app.requests.UpdateTaskStatusRequest;
import com.projectsManager.app.responses.DefaultErrorResponse;
import com.projectsManager.app.responses.DefaultResponse;
import com.projectsManager.app.responses.TaskResponse;
import com.projectsManager.app.responses.TasksResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/sprints/{sprintId}/tasks")
public class TasksController {
    private final SprintsServiceInterface sprintsService;
    private final TasksServiceInterface tasksService;
    private final UsersServiceInterface usersService;

    public TasksController(SprintsServiceInterface sprintsService, TasksServiceInterface tasksService, UsersServiceInterface usersService) {
        this.sprintsService = sprintsService;
        this.tasksService = tasksService;
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity getAll(@PathVariable("sprintId") Long sprintId) {
        try {
            Sprint sprint = sprintsService.getById(sprintId);

            List<Task> tasks = sprint.listTasks();
            var tasksAsResponse = tasks.stream().map(TaskResponse::new).toList();

            return ResponseEntity.ok().body(new TasksResponse(tasksAsResponse));
        } catch (SprintNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable("id") Long id) {
        try {
            Task task = tasksService.getById(id);

            return ResponseEntity.ok().body(new TaskResponse(task));
        } catch (TaskNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@PathVariable("sprintId") Long sprintId, @Valid @RequestBody CreateTaskRequest request) {
        try {
            Sprint sprint = sprintsService.getById(sprintId);
            User owner = usersService.getById(request.ownerId());

            Task task = new Task(request, owner, sprint);

            sprint.addTask(task);

            sprintsService.update(sprint);

            return ResponseEntity.status(201).body(new DefaultResponse("Task criada."));
        } catch (SprintNotFoundException | UserNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity updateStatus(@PathVariable("id") Long id, @Valid @RequestBody UpdateTaskStatusRequest request) {
        try {
            Task task = tasksService.getById(id);

            task.setStatus(request.newTaskStatus());

            tasksService.update(task);

            return ResponseEntity.ok().body(new DefaultResponse("Status atualizado."));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/owner")
    public ResponseEntity updateOwner(@PathVariable("id") Long id, @Valid @RequestBody UpdateTaskOwnerRequest request) {
        try {
            Task task = tasksService.getById(id);
            User newOwner = usersService.getById(request.newOwnerId());

            task.setOwner(newOwner);

            tasksService.update(task);

            return ResponseEntity.ok().body(new DefaultResponse("Responsável atualizado."));
        } catch (TaskNotFoundException | UserNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove(@PathVariable("id") Long id, @PathVariable("sprintId") Long sprintId) {
        try {
            Sprint sprint = sprintsService.getById(sprintId);

            sprint.removeTaskById(id);

            sprintsService.update(sprint);

            return ResponseEntity.ok().body(new DefaultResponse("Task removida."));
        } catch (SprintNotFoundException | TaskNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }
}
