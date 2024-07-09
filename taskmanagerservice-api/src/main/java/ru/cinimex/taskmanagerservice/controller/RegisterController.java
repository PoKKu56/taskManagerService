package ru.cinimex.taskmanagerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cinimex.taskmanagerservice.dto.RegisterConfirmationRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterResponse;

@RequestMapping("/register")
public interface RegisterController {

    @PostMapping
    ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest);

    @PostMapping("/code")
    ResponseEntity<RegisterResponse> registerCode(@RequestBody RegisterConfirmationRequest registerConfirmationRequest);
}
