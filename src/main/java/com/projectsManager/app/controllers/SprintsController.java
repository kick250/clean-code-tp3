package com.projectsManager.app.controllers;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.exceptions.SprintNotFoundException;
import com.projectsManager.app.interfaces.ProjectsServiceInterface;
import com.projectsManager.app.interfaces.SprintsServiceInterface;
import com.projectsManager.app.requests.CreateSprintRequest;
import com.projectsManager.app.requests.UpdateSprintRequest;
import com.projectsManager.app.responses.DefaultErrorResponse;
import com.projectsManager.app.responses.DefaultResponse;
import com.projectsManager.app.responses.SprintResponse;
import com.projectsManager.app.responses.SprintsResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/sprints")
public class SprintsController {
    private final ProjectsServiceInterface projectsService;
    private final SprintsServiceInterface sprintsService;

    public SprintsController(ProjectsServiceInterface projectsService, SprintsServiceInterface sprintsService) {
        this.projectsService = projectsService;
        this.sprintsService = sprintsService;
    }

    @GetMapping()
    public ResponseEntity getByProject(@PathVariable("projectId") Long projectId) {
        try {
            Project project = projectsService.getById(projectId);
            var sprints = project.listSprints();
            var springAsResponse = sprints.stream().map(SprintResponse::new).toList();
            return ResponseEntity.ok(new SprintsResponse(springAsResponse));
        } catch (ProjectNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@PathVariable("projectId") Long projectId, @Valid @RequestBody CreateSprintRequest request) {
        try {
            Project project = projectsService.getById(projectId);
            Sprint sprint = new Sprint(request);

            project.addSprint(sprint);

            projectsService.update(project);

            return ResponseEntity.status(201).body(new DefaultResponse("Sprint criada."));
        } catch (ProjectNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@PathVariable("projectId") Long projectId, @PathVariable("id") Long id, @Valid @RequestBody UpdateSprintRequest request) {
        try {
            Project project = projectsService.getById(projectId);

            Sprint sprint = new Sprint(id, request, project);

            sprintsService.update(sprint);

            return ResponseEntity.ok(new DefaultResponse("Sprint atualizada."));
        } catch (SprintNotFoundException | ProjectNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteById(@PathVariable("projectId") Long projectId, @PathVariable("id") Long id) {
        try {
            Project project = projectsService.getById(projectId);

            project.removeSprintById(id);

            projectsService.update(project);

            return ResponseEntity.ok(new DefaultResponse("Sprint deletada."));
        } catch (ProjectNotFoundException | SprintNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }
}
