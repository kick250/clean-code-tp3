package com.projectsManager.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsManager.app.domain.Project;
import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.domain.Task;
import com.projectsManager.app.domain.User;
import com.projectsManager.app.domainGenerator.ProjectGenerator;
import com.projectsManager.app.domainGenerator.SprintGenerator;
import com.projectsManager.app.domainGenerator.TaskGenerator;
import com.projectsManager.app.domainGenerator.UserGenerator;
import com.projectsManager.app.enums.TaskStatus;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.repositories.ProjectsRepository;
import com.projectsManager.app.repositories.TasksRepository;
import com.projectsManager.app.repositories.UsersRepository;
import com.projectsManager.app.requests.CreateTaskRequest;
import com.projectsManager.app.requests.UpdateTaskOwnerRequest;
import com.projectsManager.app.requests.UpdateTaskStatusRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectsRepository projectsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TasksRepository tasksRepository;

    private String uri;
    private Project project;
    private Sprint sprint;
    private Task task;
    private User user;
    private User user2;

    @BeforeEach
    public void setup() throws ProjectNotFoundException {
        projectsRepository.deleteAll();

        project = ProjectGenerator.createProject();
        sprint = SprintGenerator.createSprint();
        task = TaskGenerator.createTask();
        user = UserGenerator.createUser();
        user2 = UserGenerator.createUser();

        usersRepository.create(user);
        usersRepository.create(user2);

        sprint.addTask(task);
        task.setOwner(user);

        projectsRepository.create(project);
        project.addSprint(sprint);
        projectsRepository.update(project);;

        uri = String.format("/projects/%d/sprints/%d/tasks", project.getId(), sprint.getId());
    }

    @AfterEach
    public void teardown() {
        projectsRepository.deleteAll();
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['tasks'][0]['id']").value(task.getId()))
                .andExpect(jsonPath("$['tasks'][0]['title']").value(task.getTitle()))
                .andExpect(jsonPath("$['tasks'][0]['description']").value(task.getDescription()))
                .andExpect(jsonPath("$['tasks'][0]['ownerId']").value(task.getOwnerId()))
                .andExpect(jsonPath("$['tasks'][0]['ownerEmail']").value(task.getOwnerEmail()))
                .andExpect(jsonPath("$['tasks'][1]").doesNotExist());
    }

    @Test
    public void getAll_whenEmpty() throws Exception {
        tasksRepository.deleteAll();

        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['tasks']").isEmpty());
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get(uri + "/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['id']").value(task.getId()))
                .andExpect(jsonPath("$['title']").value(task.getTitle()))
                .andExpect(jsonPath("$['description']").value(task.getDescription()))
                .andExpect(jsonPath("$['ownerId']").value(task.getOwnerId()))
                .andExpect(jsonPath("$['ownerEmail']").value(task.getOwnerEmail()));
    }

    @Test
    public void getById_whenTaskNotFound() throws Exception {
        mockMvc.perform(get(uri + "/" + 101010L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void create() throws Exception {
        var request = new CreateTaskRequest("Titulo teste", "Descricao teste", TaskStatus.TODO, user.getId());

        assertEquals(1, sprint.listTasks().size());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['result']").value("Task criada."));

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(2, sprint.listTasks().size());
    }

    @Test
    public void create_whenSomeParamInvalid() throws Exception {
        var request = new CreateTaskRequest("", "Descricao teste", TaskStatus.TODO, user.getId());

        assertEquals(1, sprint.listTasks().size());
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(1, sprint.listTasks().size());
    }

    @Test
    public void create_whenUserDoesNotExist() throws Exception {
        var request = new CreateTaskRequest("Titulo teste", "Descricao teste", TaskStatus.TODO, 101010L);

        assertEquals(1, sprint.listTasks().size());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Esse usuário não foi encontrado."));

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(1, sprint.listTasks().size());
    }

    @Test
    public void create_whenSprintDoesNotExist() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks", project.getId(), 101010L);

        var request = new CreateTaskRequest("Titulo teste", "Descricao teste", TaskStatus.TODO, 101010L);

        assertEquals(1, sprint.listTasks().size());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Essa sprint não foi encontrada."));

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(1, sprint.listTasks().size());
    }

    @Test
    public void updateStatus() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d/status", project.getId(), sprint.getId(), task.getId());

        var request = new UpdateTaskStatusRequest(TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.TODO, task.getStatus());

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Status atualizado."));

        task = tasksRepository.getById(task.getId());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void updateStatus_whenTaskNotFound() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d/status", project.getId(), sprint.getId(), 101010L);

        var request = new UpdateTaskStatusRequest(TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.TODO, task.getStatus());

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        task = tasksRepository.getById(task.getId());
        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    public void updateOwner() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d/owner", project.getId(), sprint.getId(), task.getId());

        var request = new UpdateTaskOwnerRequest(user2.getId());

        assertNotEquals(user2.getId(), task.getOwnerId());

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Responsável atualizado."));

        task = tasksRepository.getById(task.getId());
        assertEquals(user2.getId(), task.getOwnerId());
    }

    @Test
    public void updateOwner_whenTaskNotFound() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d/owner", project.getId(), sprint.getId(), 101010L);

        var request = new UpdateTaskOwnerRequest(user2.getId());

        assertNotEquals(user2.getId(), task.getOwnerId());

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Essa task não foi encontrada."));

        task = tasksRepository.getById(task.getId());
        assertNotEquals(user2.getId(), task.getOwnerId());
    }

    @Test
    public void remove() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d", project.getId(), sprint.getId(), task.getId());

        assertEquals(1, sprint.listTasks().size());

        mockMvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Task removida."));

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(0, sprint.listTasks().size());
    }

    @Test
    public void remove_whenTaskNotFound() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d", project.getId(), sprint.getId(), 101010L);

        assertEquals(1, sprint.listTasks().size());

        mockMvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Essa task não foi encontrada."));

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(1, sprint.listTasks().size());
    }

    @Test
    public void remove_whenSprintNotFound() throws Exception {
        uri = String.format("/projects/%d/sprints/%d/tasks/%d", project.getId(), 101010L, task.getId());

        assertEquals(1, sprint.listTasks().size());

        mockMvc.perform(delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Essa sprint não foi encontrada."));

        sprint = projectsRepository.getById(project.getId()).listSprints().getFirst();
        assertEquals(1, sprint.listTasks().size());
    }
}
