package ru.cinimex.taskmanagerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cinimex.taskmanagerservice.dto.RegisterConfirmationRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterRequest;

@RequestMapping("/register")
public interface RegisterController {

    @PostMapping

    ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest);

    @PostMapping("/code")
    ResponseEntity<?> registerCode(@RequestBody RegisterConfirmationRequest registerConfirmationRequest);
}
