package com.projectsManager.app.factories;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.domain.Sprint;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SprintFactory {

    public Sprint buildFromResultSet(ResultSet resultSet) throws SQLException {
        return new Sprint(
                resultSet.getLong("ID"),
                resultSet.getDate("START_DATE").toLocalDate(),
                resultSet.getDate("END_DATE").toLocalDate()
        );
    }
}
