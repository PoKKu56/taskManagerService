package ru.cinimex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cinimex.dto.AuthRequest;
import ru.cinimex.dto.TokenResponse;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody AuthRequest authRequest);

}
