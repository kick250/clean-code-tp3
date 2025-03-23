package com.projectsManager.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsManager.app.domainGenerator.ProjectGenerator;
import com.projectsManager.app.domainGenerator.SprintGenerator;
import com.projectsManager.app.domain.Project;
import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.interfaces.ProjectsRepositoryInterface;
import com.projectsManager.app.requests.CreateSprintRequest;
import com.projectsManager.app.requests.UpdateSprintRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SprintsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectsRepositoryInterface projectsRepository;

    private String uri;
    private Project project1;
    private Project project2;
    private Sprint project1Sprint;
    private Sprint project2Sprint;

    @BeforeEach
    public void setup() throws ProjectNotFoundException {
        projectsRepository.deleteAll();

        project1 = ProjectGenerator.createProject();
        project2 = ProjectGenerator.createProject();

        project1Sprint = SprintGenerator.createSprint();
        project2Sprint = SprintGenerator.createSprint();

        projectsRepository.create(project1);
        projectsRepository.create(project2);

        project1.addSprint(project1Sprint);
        project2.addSprint(project2Sprint);

        projectsRepository.update(project1);
        projectsRepository.update(project2);

        uri = String.format("/projects/%d/sprints", project1.getId());
    }

    @AfterEach
    public void teardown() {
        projectsRepository.deleteAll();
    }

    @Test
    public void getByProject() throws Exception {
        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['sprints'][0]['id']").value(project1Sprint.getId()))
                .andExpect(jsonPath("$['sprints'][0]['startDate']").value(project1Sprint.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$['sprints'][0]['endDate']").value(project1Sprint.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(jsonPath("$['sprints'][1]").doesNotExist());
    }

    @Test
    public void getByProject_whenProjectDoesNotExist() throws Exception {
        mockMvc.perform(get("/projects/101010/sprints")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Esse projeto não foi encontrado."));
    }

    @Test
    public void create() throws Exception {
        var request = new CreateSprintRequest(LocalDate.now(), LocalDate.now().plusDays(30));

        assertEquals(1, project1.listSprints().size());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['result']").value("Sprint criada."));

        project1 = projectsRepository.getById(project1.getId());
        assertEquals(2, project1.listSprints().size());
        assertEquals(request.startDate(), project1.listSprints().getLast().getStartDate());
        assertEquals(request.endDate(), project1.listSprints().getLast().getEndDate());
    }

    @Test
    public void create_whenBodyInvalid() throws Exception {
        assertEquals(1, project1.listSprints().size());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        project1 = projectsRepository.getById(project1.getId());
        assertEquals(1, project1.listSprints().size());
    }

    @Test
    public void create_whenProjectDoesNotExist() throws Exception {
        var request = new CreateSprintRequest(LocalDate.now(), LocalDate.now().plusDays(30));

        mockMvc.perform(post("/projects/101010/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Esse projeto não foi encontrado."));
    }

    @Test
    public void update() throws Exception {
        var request = new UpdateSprintRequest(LocalDate.now(), LocalDate.now().plusDays(30));

        assertEquals(1, project1.listSprints().size());
        assertNotEquals(request.startDate(), project1.listSprints().getFirst().getStartDate());
        assertNotEquals(request.endDate(), project1.listSprints().getFirst().getStartDate());

        mockMvc.perform(put(uri + "/" + project1Sprint.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Sprint atualizada."));

        project1 = projectsRepository.getById(project1.getId());

        assertEquals(1, project1.listSprints().size());
        assertEquals(request.startDate(), project1.listSprints().getFirst().getStartDate());
        assertEquals(request.endDate(), project1.listSprints().getFirst().getEndDate());
    }

    @Test
    public void update_whenSprintDoesNotExist() throws Exception {
        var request = new UpdateSprintRequest(LocalDate.now(), LocalDate.now().plusDays(30));

        assertEquals(1, project1.listSprints().size());
        assertNotEquals(request.startDate(), project1.listSprints().getFirst().getStartDate());
        assertNotEquals(request.endDate(), project1.listSprints().getFirst().getStartDate());

        mockMvc.perform(put(uri + "/" + 101010L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Essa sprint não foi encontrada."));

        project1 = projectsRepository.getById(project1.getId());

        assertEquals(1, project1.listSprints().size());
        assertNotEquals(request.startDate(), project1.listSprints().getFirst().getStartDate());
        assertNotEquals(request.endDate(), project1.listSprints().getFirst().getEndDate());
    }

    @Test
    public void update_whenProjectDoesNotExist() throws Exception {
        var request = new UpdateSprintRequest(LocalDate.now(), LocalDate.now().plusDays(30));

        assertEquals(1, project1.listSprints().size());
        assertNotEquals(request.startDate(), project1.listSprints().getFirst().getStartDate());
        assertNotEquals(request.endDate(), project1.listSprints().getFirst().getStartDate());

        mockMvc.perform(put("/projects/101010/sprints" + "/" + project1Sprint.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Esse projeto não foi encontrado."));

        project1 = projectsRepository.getById(project1.getId());

        assertEquals(1, project1.listSprints().size());
        assertNotEquals(request.startDate(), project1.listSprints().getFirst().getStartDate());
        assertNotEquals(request.endDate(), project1.listSprints().getFirst().getEndDate());
    }

    @Test
    public void deleteById() throws Exception {
        assertEquals(1, project1.listSprints().size());

        mockMvc.perform(delete(uri + "/" + project1Sprint.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Sprint deletada."));

        project1 = projectsRepository.getById(project1.getId());

        assertEquals(0, project1.listSprints().size());
    }

    @Test
    public void deleteById_whenSprintNotFound() throws Exception {
        assertEquals(1, project1.listSprints().size());

        mockMvc.perform(delete(uri + "/" + 101010L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Essa sprint não foi encontrada."));

        project1 = projectsRepository.getById(project1.getId());

        assertEquals(1, project1.listSprints().size());
    }

    @Test
    public void deleteById_whenProjectNotFound() throws Exception {
        assertEquals(1, project1.listSprints().size());
        assertEquals(1, project2.listSprints().size());

        mockMvc.perform(delete("/projects/101010/sprints" + "/" + project1Sprint.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Esse projeto não foi encontrado."));

        project1 = projectsRepository.getById(project1.getId());
        project2 = projectsRepository.getById(project2.getId());

        assertEquals(1, project1.listSprints().size());
        assertEquals(1, project2.listSprints().size());
    }
}
