package ru.cinimex.taskmanagerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cinimex.taskmanagerservice.dto.*;

import java.util.UUID;

public interface AuthController {

    @PostMapping("/auth/login")
    ResponseEntity<TokenResponse> login(@RequestBody AuthRequest authRequest);

    @GetMapping("/users/{token}")
    ResponseEntity<CurrentUserResponse> getCurrentUser(@PathVariable("token") String token);

    @GetMapping("/admin/user/{id}")
    ResponseEntity<CurrentUserResponse> getAdminUser(@PathVariable("id") UUID id);

}
