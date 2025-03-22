package com.projectsManager.app.factories;

import com.projectsManager.app.domain.Project;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Component
public class ProjectFactory {
    public Project buildFromResultSet(ResultSet resultSet) throws SQLException {
        return new Project(
            resultSet.getLong("ID"),
            resultSet.getString("NAME"),
            resultSet.getString("DESCRIPTION")
        );
    }
}
