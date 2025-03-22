package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.exceptions.ProjectNotFoundException;

import java.util.List;

public interface ProjectsServiceInterface {

    public List<Project> getAll();

    public Project getById(Long id) throws ProjectNotFoundException;

    public void create(Project project);

    public void update(Project project) throws ProjectNotFoundException;

    public void deleteById(Long id) throws ProjectNotFoundException;
}
