package com.projectsManager.app.responses;

import com.projectsManager.app.domain.Sprint;

import java.util.List;

public record SprintsResponse(List<SprintResponse> sprints) {
}
