package com.projectsManager.app.repositories;

import com.projectsManager.app.domain.Project;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.exceptions.SaveErrorException;
import com.projectsManager.app.factories.ProjectFactory;
import com.projectsManager.app.interfaces.ProjectsRepositoryInterface;
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
public class ProjectsRepository implements ProjectsRepositoryInterface {
    private final JdbcTemplate jdbcTemplate;
    private final ProjectFactory projectFactory;
    private final SprintsRepository sprintsRepository;

    public ProjectsRepository(JdbcTemplate jdbcTemplate, ProjectFactory projectFactory, SprintsRepository sprintsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectFactory = projectFactory;
        this.sprintsRepository = sprintsRepository;
    }

    @Override
    public List<Project> getAll() {
        try {
            String query = "SELECT id, name, description FROM projects";
            return jdbcTemplate.query(query, (resultSet, rowNum) -> {
                Long id = resultSet.getLong("ID");
                return projectFactory.buildFromResultSet(resultSet, sprintsRepository.getByProjectId(id));
            });
        } catch (EmptyResultDataAccessException exception) {
            return new ArrayList<>();
        }
    }

    @Override
    public Project getById(Long id) throws ProjectNotFoundException {
        try {
            String query = "SELECT id, name, description FROM projects where id = ?";
            return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                return projectFactory.buildFromResultSet(resultSet, sprintsRepository.getByProjectId(id));
            }, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ProjectNotFoundException();
        }
    }

    @Override
    public void create(Project project) {
        String sql = "INSERT INTO projects (name, description) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            return preparedStatement;
        }, keyHolder);

        if (keyHolder.getKey() == null)  throw new SaveErrorException();

        project.setAsSaved(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Project project) throws ProjectNotFoundException {
        if (!existsById(project.getId())) throw new ProjectNotFoundException();

        String sql = "UPDATE projects SET name = ?, description = ? WHERE id = ?";

        jdbcTemplate.update(sql, project.getName(), project.getDescription(), project.getId());

        sprintsRepository.saveCollection(project.listSprints());
    }

    @Override
    public void deleteById(Long id) throws ProjectNotFoundException {
        if (!existsById(id)) throw new ProjectNotFoundException();

        String sql = "DELETE FROM projects WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        sprintsRepository.deleteAll();

        String sql = "DELETE FROM projects";

        jdbcTemplate.update(sql);
    }

    private boolean existsById(Long id) {
        if (id == null) return false;

        String query = "SELECT COUNT(*) FROM projects WHERE id = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);

        return count != null && count > 0;
    }
}
