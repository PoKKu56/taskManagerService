package ru.cinimex.taskmanagerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskmanagerservice.dto.RegisterConfirmationRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterResponse;
import ru.cinimex.taskmanagerservice.service.UserService;
import ru.cinimex.taskmanagerservice.util.RegisterError;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RegisterControllerImpl implements RegisterController {

    private final UserService userService;

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        return userService.convertAndSaveUser(registerRequest);
    }

    @Override
    public ResponseEntity<?> registerCode(RegisterConfirmationRequest registerConfirmationRequest) {
        return userService.checkEmailCode(registerConfirmationRequest);
    }

}
