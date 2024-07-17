package ru.cinimex.taskmanagerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskmanagerservice.dto.*;
import ru.cinimex.taskmanagerservice.service.JwtService;
import ru.cinimex.taskmanagerservice.service.UserService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public ResponseEntity<TokenResponse> login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(authentication)));
    }

    @Override
    public ResponseEntity<?> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Override
    public ResponseEntity<?> getAdminUser(UUID id) {
        return userService.getCurrentUserById(id);
    }
}
