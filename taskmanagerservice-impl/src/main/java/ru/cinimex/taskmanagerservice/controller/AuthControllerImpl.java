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

        System.out.println(authentication);
        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(authentication)));
    }

    @Override
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {

        Optional<UserEntity> user = userService.getCurrentUser();

        if (user.isPresent()) {
            return ResponseEntity.ok(new CurrentUserResponse(user.get().getUsername(),user.get().getEmail(),
                    user.get().getRole()));
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<CurrentUserResponse> getAdminUser(Integer id) {

        Optional<UserEntity> user = userService.getCurrentUser();

        if (user.isPresent()) {
            return ResponseEntity.ok(new CurrentUserResponse(user.get().getUsername(), user.get().getEmail(),
                    user.get().getRole()));
        }

        return ResponseEntity.notFound().build();
    }
}
