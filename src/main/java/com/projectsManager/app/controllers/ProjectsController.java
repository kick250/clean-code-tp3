package com.projectsManager.app.controllers;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.interfaces.ProjectsServiceInterface;
import com.projectsManager.app.requests.CreateProjectRequest;
import com.projectsManager.app.requests.UpdateProjectRequest;
import com.projectsManager.app.responses.DefaultResponse;
import com.projectsManager.app.responses.ProjectResponse;
import com.projectsManager.app.responses.ProjectsResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectsController {
    private final ProjectsServiceInterface projectsService;

    @Autowired
    public ProjectsController(ProjectsServiceInterface projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping
    public ResponseEntity<ProjectsResponse> getAll() {
        var projects = projectsService.getAll();
        var projectsAsResponse = projects.stream().map(ProjectResponse::new).toList();
        return ResponseEntity.ok(new ProjectsResponse(projectsAsResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable("id") Long id) {
        try {
            Project project = projectsService.getById(id);
            return ResponseEntity.ok(new ProjectResponse(project));
        } catch (ProjectNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DefaultResponse> create(@Valid @RequestBody CreateProjectRequest request) {
        Project project = new Project(request);
        projectsService.create(project);
        return ResponseEntity.status(201).body(new DefaultResponse("Projeto criado."));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DefaultResponse> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateProjectRequest request) {
        try {
            Project project = new Project(id, request);
            projectsService.update(project);
            return ResponseEntity.status(200).body(new DefaultResponse("Projeto atualizado."));
        } catch (ProjectNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<DefaultResponse> deleteById(@PathVariable("id") Long id) {
        try {
            projectsService.deleteById(id);
            return ResponseEntity.status(200).body(new DefaultResponse("Projeto deletado."));
        } catch (ProjectNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}