package com.projectsManager.app.repositories;

import com.projectsManager.app.domain.Task;
import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.SaveErrorException;
import com.projectsManager.app.exceptions.TaskNotFoundException;
import com.projectsManager.app.exceptions.UserNotFoundException;
import com.projectsManager.app.factories.TaskFactory;
import com.projectsManager.app.interfaces.TasksRepositoryInterface;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TasksRepository implements TasksRepositoryInterface {
    private final JdbcTemplate jdbcTemplate;
    private final TaskFactory taskFactory;
    private final UsersRepository usersRepository;

    public TasksRepository(JdbcTemplate jdbcTemplate, TaskFactory taskFactory, UsersRepository usersRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskFactory = taskFactory;
        this.usersRepository = usersRepository;
    }

    public List<Task> getBySprintId(Long sprintId) {
        try {
            String query = "SELECT id, title, description, status, owner_id FROM tasks WHERE sprint_id = ?";
            return jdbcTemplate.query(query, (resultSet, rowNum) -> {
                Long ownerId = resultSet.getLong("OWNER_ID");
                return taskFactory.buildFromResultSet(resultSet, tryGetOwner(ownerId));
            }, sprintId);
        } catch (EmptyResultDataAccessException exception) {
            return new ArrayList<>();
        }
    }

    private User tryGetOwner(Long ownerId) {
        try {
            return usersRepository.getById(ownerId);
        } catch (UserNotFoundException ignore) {
            return null;
        }
    }

    @Override
    public Task getById(Long id) throws TaskNotFoundException {
        try {
            String query = "SELECT id, title, description, status, owner_id FROM tasks WHERE id = ?";
            return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                Long ownerId = resultSet.getLong("OWNER_ID");
                return taskFactory.buildFromResultSet(resultSet, tryGetOwner(ownerId));
            }, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new TaskNotFoundException();
        }
    }

    public void saveCollection(List<Task> tasks) {
        if (tasks == null) return;

        for (Task task : tasks)
            save(task);
    }

    public void save(Task task) {
        if (task.isMarkedForDelete()) {
            tryDelete(task);
            return;
        }

        try {
            update(task);
        } catch (TaskNotFoundException exception){
            create(task);
        }
    }

    public void create(Task task) {
        String sql = "INSERT INTO tasks (title, description, status, owner_id, sprint_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus().toString());
            preparedStatement.setLong(4, task.getOwnerId());
            preparedStatement.setLong(5, task.getSpringId());
            return preparedStatement;
        }, keyHolder);

        if (keyHolder.getKey() == null)  throw new SaveErrorException();

        task.setAsSaved(keyHolder.getKey().longValue());
    }

    public void update(Task task) throws TaskNotFoundException {
        if (!existsById(task.getId())) throw new TaskNotFoundException();

        if (task.hasSprint()) {
            updateWithSprint(task);
            return;
        }
        updateWithoutSprint(task);
    }

    public void updateWithSprint(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, owner_id = ?, sprint_id = ?, WHERE id = ?";

        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getOwnerId(), task.getSpringId(), task.getId());
    }

    public void updateWithoutSprint(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, owner_id = ? WHERE id = ?";

        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getOwnerId(), task.getId());
    }

    private void tryDelete(Task task) {
        try {
            delete(task);
        } catch (TaskNotFoundException ignored) {
            return;
        }
    }

    public void delete(Task task) throws TaskNotFoundException {
        if (!existsById(task.getId())) throw new TaskNotFoundException();

        String sql = "DELETE FROM tasks WHERE id = ?";

        jdbcTemplate.update(sql, task.getId());
    }

    private boolean existsById(Long id) {
        if (id == null) return false;

        String query = "SELECT COUNT(*) FROM tasks WHERE id = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);

        return count != null && count > 0;
    }

    public void deleteAll() {
        String sql = "DELETE FROM tasks";

        jdbcTemplate.update(sql);
    }
}
