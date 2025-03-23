package com.projectsManager.app.repositories;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.SaveErrorException;
import com.projectsManager.app.exceptions.UserNotFoundException;
import com.projectsManager.app.factories.UserFactory;
import com.projectsManager.app.interfaces.UsersRepositoryInterface;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UsersRepository implements UsersRepositoryInterface {
    private final JdbcTemplate jdbcTemplate;
    private final UserFactory userFactory;

    public UsersRepository(JdbcTemplate jdbcTemplate, UserFactory userFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.userFactory = userFactory;
    }

    public User getById(Long id) throws UserNotFoundException {
        try {
            String query = "SELECT id, name, email, position FROM users WHERE id = ?";
            return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                return userFactory.buildFromResultSet(resultSet);
            }, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException();
        }
    }

    public void create(User user) {
        String sql = "INSERT INTO users (name, email, position) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPosition().toString());
            return preparedStatement;
        }, keyHolder);

        if (keyHolder.getKey() == null)  throw new SaveErrorException();

        user.setAsSaved(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        if (!existsById(user.getId())) throw new UserNotFoundException();

        String sql = "UPDATE users SET name = ?, email = ?, position = ? WHERE id = ?";

        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPosition().toString(), user.getId());
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM users";

        jdbcTemplate.update(sql);
    }

    private boolean existsById(Long id) {
        if (id == null) return false;

        String query = "SELECT COUNT(*) FROM users WHERE id = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);

        return count != null && count > 0;
    }

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM users";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class);

        if (count == null) return 0;

        return count;
    }
}
