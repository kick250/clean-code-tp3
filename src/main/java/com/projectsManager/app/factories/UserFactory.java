package com.projectsManager.app.factories;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.enums.UserPosition;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserFactory {
    public User buildFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("ID"),
                resultSet.getString("NAME"),
                resultSet.getString("EMAIL"),
                convertPosition(resultSet.getString("POSITION"))
        );
    }

    private UserPosition convertPosition(String position) {
        return UserPosition.valueOf(position);
    }
}
