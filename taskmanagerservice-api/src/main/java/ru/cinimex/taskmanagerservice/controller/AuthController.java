package ru.cinimex.taskmanagerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cinimex.taskmanagerservice.dto.*;

public interface AuthController {

    @PostMapping("/auth/login")
    ResponseEntity<TokenResponse> login(@RequestBody AuthRequest authRequest);

    @GetMapping("/users/current")
    ResponseEntity<CurrentUserResponse> getCurrentUser();

    @GetMapping("/admin/user/{id}")
    ResponseEntity<CurrentUserResponse> getAdminUser(@PathVariable("id") Integer id);

}
