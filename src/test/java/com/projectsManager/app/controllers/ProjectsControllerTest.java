package com.projectsManager.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsManager.app.DomainGenerator.ProjectGenerator;
import com.projectsManager.app.domain.Project;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.interfaces.ProjectsRepositoryInterface;
import com.projectsManager.app.requests.CreateProjectRequest;
import com.projectsManager.app.requests.UpdateProjectRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String PATH = "/projects";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProjectsRepositoryInterface projectsRepository;

    private Project project1;
    private Project project2;

    @BeforeEach
    public void setup() {
        projectsRepository.deleteAll();

        project1 = ProjectGenerator.createProject();
        project2 = ProjectGenerator.createProject();

        projectsRepository.create(project1);
        projectsRepository.create(project2);
    }

    @AfterEach
    public void teardown() {
        projectsRepository.deleteAll();
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['projects'][0]['id']").value(project1.getId()))
                .andExpect(jsonPath("$['projects'][0]['name']").value(project1.getName()))
                .andExpect(jsonPath("$['projects'][0]['description']").value(project1.getDescription()))
                .andExpect(jsonPath("$['projects'][1]['id']").value(project2.getId()))
                .andExpect(jsonPath("$['projects'][1]['name']").value(project2.getName()))
                .andExpect(jsonPath("$['projects'][1]['description']").value(project2.getDescription()));
    }

    @Test
    public void getAll_whenEmpty() throws Exception {
        projectsRepository.deleteAll();

        mockMvc.perform(get(PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['projects']").isEmpty());
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get(PATH + "/" + project1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['id']").value(project1.getId()))
                .andExpect(jsonPath("$['name']").value(project1.getName()))
                .andExpect(jsonPath("$['description']").value(project1.getDescription()));
    }

    @Test
    public void getById_whenNotFound() throws Exception {
        mockMvc.perform(get(PATH + "/" + 101010L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void create() throws  Exception {
        projectsRepository.deleteAll();

        var request = new CreateProjectRequest("Teste de nome", "Teste de descricao");

        assertEquals(0, projectsRepository.getAll().size());

        mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertEquals(1, projectsRepository.getAll().size());
    }

    @Test
    public void create_whenBodyIsInvalid() throws  Exception {
        projectsRepository.deleteAll();

        var request = new CreateProjectRequest("", "");

        assertEquals(0, projectsRepository.getAll().size());

        mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(0, projectsRepository.getAll().size());
    }

    @Test
    public void update() throws  Exception {
        var request = new UpdateProjectRequest("Novo nome", "Nova descricao");

        mockMvc.perform(put(PATH + "/" + project1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        project1 = projectsRepository.getById(project1.getId());
        assertEquals(project1.getName(), request.name());
        assertEquals(project1.getDescription(), request.description());
    }

    @Test
    public void update_whenInvalid() throws  Exception {
        var request = new UpdateProjectRequest("", "");

        mockMvc.perform(put(PATH + "/" + project1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        project1 = projectsRepository.getById(project1.getId());
        assertNotEquals(project1.getName(), request.name());
        assertNotEquals(project1.getDescription(), request.description());
    }

    @Test
    public void update_whenNotFound() throws  Exception {
        var request = new UpdateProjectRequest("teste", "teste");

        mockMvc.perform(put(PATH + "/" + 101010L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteById() throws  Exception {
        assertEquals(2, projectsRepository.getAll().size());

        mockMvc.perform(delete(PATH + "/" + project1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, projectsRepository.getAll().size());
        assertThrows(ProjectNotFoundException.class, () -> projectsRepository.getById(project1.getId()));
    }

    @Test
    public void deleteById_whenNotFound() throws  Exception {
        assertEquals(2, projectsRepository.getAll().size());

        mockMvc.perform(delete(PATH + "/" + 101010L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertEquals(2, projectsRepository.getAll().size());
    }
}
