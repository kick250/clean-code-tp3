package com.projectsManager.app.factories;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.domain.Task;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SprintFactory {

    public Sprint buildFromResultSet(ResultSet resultSet, List<Task> tasks) throws SQLException {
        return new Sprint(
                resultSet.getLong("ID"),
                resultSet.getDate("START_DATE").toLocalDate(),
                resultSet.getDate("END_DATE").toLocalDate(),
                tasks
        );
    }
}
