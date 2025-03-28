package com.projectsManager.app.repositories;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.SaveErrorException;
import com.projectsManager.app.exceptions.SprintNotFoundException;
import com.projectsManager.app.factories.SprintFactory;
import com.projectsManager.app.interfaces.SprintsRepositoryInterface;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SprintsRepository implements SprintsRepositoryInterface {
    private final JdbcTemplate jdbcTemplate;
    private final SprintFactory sprintFactory;
    private final TasksRepository tasksRepository;

    public SprintsRepository(JdbcTemplate jdbcTemplate, SprintFactory sprintFactory, TasksRepository tasksRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.sprintFactory = sprintFactory;
        this.tasksRepository  = tasksRepository;
    }

    public List<Sprint> getByProjectId(Long projectId) {
        try {
            String query = "SELECT id, start_date, end_date FROM sprints WHERE project_id = ?";
            return jdbcTemplate.query(query, (resultSet, rowNum) -> {
                Long id = resultSet.getLong("ID");
                return sprintFactory.buildFromResultSet(resultSet, tasksRepository.getBySprintId(id));
            }, projectId);
        } catch (EmptyResultDataAccessException exception) {
            return new ArrayList<>();
        }
    }

    public Sprint getById(Long id) throws SprintNotFoundException {
        try {
            String query = "SELECT id, start_date, end_date FROM sprints WHERE id = ?";
            return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                return sprintFactory.buildFromResultSet(resultSet, tasksRepository.getBySprintId(id));
            }, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new SprintNotFoundException();
        }
    }

    public void saveCollection(List<Sprint> sprints) {
        if (sprints == null) return;

        for (Sprint sprint : sprints)
            save(sprint);
    }

    private void save(Sprint sprint) {
        if (sprint.isMarkedForDelete()) {
            tryDelete(sprint);
            return;
        }

        try {
            update(sprint);
        } catch (SprintNotFoundException exception){
            create(sprint);
        }
    }

    public void create(Sprint sprint) {
        String sql = "INSERT INTO sprints (start_date, end_date, project_id) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(sprint.getStartDate()));
            preparedStatement.setDate(2, Date.valueOf(sprint.getEndDate()));
            preparedStatement.setLong(3, sprint.getProjectId());
            return preparedStatement;
        }, keyHolder);

        if (keyHolder.getKey() == null)  throw new SaveErrorException();

        sprint.setAsSaved(keyHolder.getKey().longValue());

        tasksRepository.saveCollection(sprint.listTasks());
    }

    public void update(Sprint sprint) throws SprintNotFoundException {
        if (!existsById(sprint.getId())) throw new SprintNotFoundException();

        if (sprint.hasProject())
            updateWithProject(sprint);
        else
            updateWithoutProject(sprint);

        tasksRepository.saveCollection(sprint.listTasks());
    }

    private void updateWithProject(Sprint sprint) {
        String sql = "UPDATE sprints SET start_date = ?, end_date = ?, project_id = ? WHERE id = ?";

        jdbcTemplate.update(sql, sprint.getStartDate(), sprint.getEndDate(), sprint.getProjectId(), sprint.getId());
    }

    private void updateWithoutProject(Sprint sprint) {
        String sql = "UPDATE sprints SET start_date = ?, end_date = ? WHERE id = ?";

        jdbcTemplate.update(sql, sprint.getStartDate(), sprint.getEndDate(), sprint.getId());
    }

    public void tryDelete(Sprint sprint) {
        try {
            delete(sprint);
        } catch (SprintNotFoundException ignored) {
        }
    }

    public void delete(Sprint sprint) throws SprintNotFoundException {
        if (!existsById(sprint.getId())) throw new SprintNotFoundException();

        String sql = "DELETE FROM sprints WHERE id = ?";

        jdbcTemplate.update(sql, sprint.getId());
    }

    public void deleteAll() {
        tasksRepository.deleteAll();

        String sql = "DELETE FROM sprints";

        jdbcTemplate.update(sql);
    }

    private boolean existsById(Long id) {
        if (id == null) return false;

        String query = "SELECT COUNT(*) FROM sprints WHERE id = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);

        return count != null && count > 0;
    }
}
