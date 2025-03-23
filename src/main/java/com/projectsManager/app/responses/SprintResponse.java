package com.projectsManager.app.responses;

import com.projectsManager.app.domain.Sprint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record SprintResponse(Long id, String startDate, String endDate) {
    public SprintResponse(Sprint sprint) {
        this(sprint.getId(), formatDate(sprint.getStartDate()), formatDate(sprint.getEndDate()));
    }

    private static String formatDate(LocalDate date) {
        if (date == null) return null;

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
