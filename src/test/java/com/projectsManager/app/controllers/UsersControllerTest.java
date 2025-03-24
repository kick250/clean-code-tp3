package com.projectsManager.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsManager.app.domain.User;
import com.projectsManager.app.domainGenerator.UserGenerator;
import com.projectsManager.app.enums.UserPosition;
import com.projectsManager.app.exceptions.ProjectNotFoundException;
import com.projectsManager.app.interfaces.ProjectsRepositoryInterface;
import com.projectsManager.app.interfaces.UsersRepositoryInterface;
import com.projectsManager.app.requests.CreateUserRequest;
import com.projectsManager.app.requests.UpdateUserEmailRequest;
import com.projectsManager.app.requests.UpdateUserPositionRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepositoryInterface usersRepository;
    @Autowired
    private ProjectsRepositoryInterface projectsRepository;

    private User user;
    private String uri;

    @BeforeEach
    public void setup() throws ProjectNotFoundException {
        usersRepository.deleteAll();

        user = UserGenerator.createUser();

        user = usersRepository.create(user);

        uri = "/users";
    }

    @AfterEach
    public void teardown() {
        projectsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    public void create() throws Exception {
        var request = new CreateUserRequest("Ana Silva", "ana.silva@gmail.com.br", UserPosition.DEVELOPER);

        assertEquals(1, usersRepository.count());

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['result']").value("Usuário criado."));

        assertEquals(2, usersRepository.count());
    }

    @Test
    public void create_whenEmailIsInvalid() throws Exception {
        var request = new CreateUserRequest("Ana Silva", "ana.silvagmail.com.br", UserPosition.DEVELOPER);

        assertEquals(1, usersRepository.count());

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(1, usersRepository.count());
    }

    @Test
    public void create_whenParamsAreInvalids() throws Exception {
        var request = new CreateUserRequest("", "", UserPosition.DEVELOPER);

        assertEquals(1, usersRepository.count());

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(1, usersRepository.count());
    }

    @Test
    public void updateEmail() throws Exception {
        uri = String.format("/users/%d/email", user.getId());
        var request = new UpdateUserEmailRequest("teste@test.com.br");

        assertNotEquals(request.newEmail(), user.getEmail());

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Email atualizado."));

        user = usersRepository.getById(user.getId());

        assertEquals(request.newEmail(), user.getEmail());
    }

    @Test
    public void updateEmail_whenEmailIsInvalid() throws Exception {
        uri = String.format("/users/%d/email", user.getId());
        var request = new UpdateUserEmailRequest("testetest");

        assertNotEquals(request.newEmail(), user.getEmail());

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        user = usersRepository.getById(user.getId());

        assertNotEquals(request.newEmail(), user.getEmail());
    }

    @Test
    public void updateEmail_whenUserDoesNotExist() throws Exception {
        uri = String.format("/users/%d/email", 101010L);
        var request = new UpdateUserEmailRequest("teste@test.com.br");

        assertNotEquals(request.newEmail(), user.getEmail());

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        user = usersRepository.getById(user.getId());

        assertNotEquals(request.newEmail(), user.getEmail());
    }

    @Test
    public void updatePosition() throws Exception {
        uri = String.format("/users/%d/position", user.getId());
        var request = new UpdateUserPositionRequest(UserPosition.DESIGNER);

        assertNotEquals(request.newPosition(), user.getPosition());

        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['result']").value("Cargo atualizado."));

        user = usersRepository.getById(user.getId());

        assertEquals(request.newPosition(), user.getPosition());
    }

    @Test
    public void updatePosition_whenUserDoesNotExist() throws Exception {
        uri = String.format("/users/%d/position", 101010L);
        var request = new UpdateUserPositionRequest(UserPosition.DESIGNER);

        assertNotEquals(request.newPosition(), user.getPosition());

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        user = usersRepository.getById(user.getId());

        assertNotEquals(request.newPosition(), user.getPosition());
    }
}
