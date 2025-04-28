package ru.cinimex.taskmanagerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cinimex.taskmanagerservice.dto.*;

import java.util.UUID;

public interface AuthController {

    @PostMapping("/auth/login")
    ResponseEntity<TokenResponse> login(@RequestBody AuthRequest authRequest);

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/users")
    ResponseEntity<CurrentUserResponse> getCurrentUser();

    @PreAuthorize("hasRole('ADMIN') or hasRole('TECH')")
    @GetMapping("/admin/user/{id}")
    ResponseEntity<CurrentUserResponse> getAdminUser(@PathVariable("id") UUID id);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/tech/token")
    ResponseEntity<TokenResponse> generateTechToken(@RequestBody CreateTechTokenRequest createTechTokenRequest);

}
