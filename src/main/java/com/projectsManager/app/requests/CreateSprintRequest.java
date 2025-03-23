package com.projectsManager.app.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSprintRequest(
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate
) {

}
