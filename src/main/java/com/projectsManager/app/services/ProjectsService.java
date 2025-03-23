package com.projectsManager.app.services;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.exceptions.SprintNotFoundException;
import com.projectsManager.app.interfaces.ProjectsRepositoryInterface;
import com.projectsManager.app.interfaces.ProjectsServiceInterface;
import com.projectsManager.app.repositories.ProjectsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsService implements ProjectsServiceInterface {
    private final ProjectsRepositoryInterface projectsRepository;

    public ProjectsService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    @Override
    public List<Project> getAll() {
        return projectsRepository.getAll();
    }

    @Override
    public Project getById(Long id) throws ProjectNotFoundException {
        return projectsRepository.getById(id);
    }

    @Override
    public void create(Project project) {
        projectsRepository.create(project);
    }

    @Override
    public void update(Project project) throws ProjectNotFoundException {
        projectsRepository.update(project);
    }

    @Override
    public void deleteById(Long id) throws ProjectNotFoundException {
        projectsRepository.deleteById(id);
    }
}
