package ru.cinimex.taskmanagerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public ResponseEntity<TokenResponse> login(AuthRequest authRequest) {
        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(
                userService.loginUser(authRequest))));
    }

    @Override
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    public ResponseEntity<CurrentUserResponse> getAdminUser(UUID id) {
        return ResponseEntity.ok(userService.getCurrentUserById(id));
    }

    @Override
    public ResponseEntity<TokenResponse> generateTechToken(CreateTechTokenRequest createTechTokenRequest) {
        return ResponseEntity.ok(userService.createTechToken(createTechTokenRequest));
    }
}
