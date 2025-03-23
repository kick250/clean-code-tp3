package com.projectsManager.app.factories;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.domain.Sprint;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProjectFactory {
    public Project buildFromResultSet(ResultSet resultSet, List<Sprint> sprints) throws SQLException {
        Project project = new Project(
            resultSet.getLong("ID"),
            resultSet.getString("NAME"),
            resultSet.getString("DESCRIPTION")
        );

        for (Sprint sprint : sprints)
            project.addSprint(sprint);

        return project;
    }
}
