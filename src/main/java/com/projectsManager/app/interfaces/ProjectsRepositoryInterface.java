package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.exceptions.ProjectNotFoundException;

import java.util.List;

public interface ProjectsRepositoryInterface {

    List<Project> getAll();

    Project getById(Long id) throws ProjectNotFoundException;

    void create(Project project);

    void update(Project project) throws ProjectNotFoundException;

    void deleteById(Long id) throws ProjectNotFoundException;

    void deleteAll();
}
