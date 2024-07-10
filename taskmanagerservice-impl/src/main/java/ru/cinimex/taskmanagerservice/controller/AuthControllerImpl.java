package ru.cinimex.taskmanagerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskmanagerservice.domain.UserEntity;
import ru.cinimex.taskmanagerservice.dto.*;
import ru.cinimex.taskmanagerservice.service.JwtService;
import ru.cinimex.taskmanagerservice.service.UserService;

import java.sql.SQLOutput;
import java.util.Optional;
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
    public ResponseEntity<CurrentUserResponse> getCurrentUser(String token) {

        Optional<UserEntity> user = userService.getCurrentUser(token);

        return user.map(userEntity -> ResponseEntity.ok(new CurrentUserResponse(userEntity.getUsername(),
                userEntity.getEmail(), userEntity.getRole()))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CurrentUserResponse> getAdminUser(UUID id) {

        Optional<UserEntity> user = userService.getCurrentUserById(id);

        return user.map(userEntity -> ResponseEntity.ok(new CurrentUserResponse(userEntity.getUsername(),
                userEntity.getEmail(), userEntity.getRole()))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
