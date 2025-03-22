package com.projectsManager.app.responses;

import com.projectsManager.app.domain.Project;

import java.util.List;

public record ProjectsResponse(List<ProjectResponse> projects) {
}
