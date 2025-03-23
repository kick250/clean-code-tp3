package com.projectsManager.app.factories;

import com.projectsManager.app.domain.Task;
import com.projectsManager.app.domain.User;
import com.projectsManager.app.enums.TaskStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TaskFactory {
    public Task buildFromResultSet(ResultSet resultSet, User owner) throws SQLException {
        return new Task(
                resultSet.getLong("ID"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                convertStatus(resultSet.getString("status")),
                owner
        );
    }

    private TaskStatus convertStatus(String status) {
        return TaskStatus.valueOf(status);
    }
}
