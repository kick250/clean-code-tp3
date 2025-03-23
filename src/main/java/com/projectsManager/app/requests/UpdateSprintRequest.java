package com.projectsManager.app.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateSprintRequest(
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate
) {
}
