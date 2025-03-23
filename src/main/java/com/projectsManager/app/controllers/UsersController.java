package com.projectsManager.app.controllers;

import com.projectsManager.app.domain.User;
import com.projectsManager.app.exceptions.UserNotFoundException;
import com.projectsManager.app.interfaces.UsersServiceInterface;
import com.projectsManager.app.requests.CreateUserRequest;
import com.projectsManager.app.requests.UpdateUserEmailRequest;
import com.projectsManager.app.requests.UpdateUserPositionRequest;
import com.projectsManager.app.responses.DefaultResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersServiceInterface usersService;

    public UsersController(UsersServiceInterface usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DefaultResponse> create(@Valid @RequestBody CreateUserRequest request) {
        User user = new User(request);

        usersService.create(user);
        return ResponseEntity.status(201).body(new DefaultResponse("Usuário criado."));
    }

    @PutMapping("/{id}/email")
    @Transactional
    public ResponseEntity<DefaultResponse> updateEmail(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserEmailRequest request) {
        try {
            User user = usersService.getById(id);

            user.updateEmail(request.newEmail());

            usersService.update(user);

            return ResponseEntity.ok().body(new DefaultResponse("Email atualizado."));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/position")
    @Transactional
    public ResponseEntity<DefaultResponse> updatePosition(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserPositionRequest request) {
        try {
            User user = usersService.getById(id);

            user.setPosition(request.newPosition());

            usersService.update(user);

            return ResponseEntity.ok().body(new DefaultResponse("Cargo atualizado."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
