package ru.cinimex.taskmanagerservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @SneakyThrows
    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) {


        if (userService.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RegisterError("Пользователь с таким логином уже зарегестрирован");
        }
        else if(userService.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new RegisterError("Пользователь с таким email уже зарегестрирован");
        }

        userService.convertAndSaveUser(registerRequest);

        return ResponseEntity.ok(new RegisterResponse("Успешное создание поля User"));
    }

    @Override
    public ResponseEntity<RegisterResponse> registerCode(RegisterConfirmationRequest registerConfirmationRequest) {
        userService.checkEmailCode(registerConfirmationRequest);
        return ResponseEntity.ok(new RegisterResponse("Успешная регистрация"));
    }

    @ExceptionHandler(RegisterError.class)
    ResponseEntity<String> registerError(final RegisterError registerError) {
        return new ResponseEntity<>(registerError.getMessage(), HttpStatus.CONFLICT);
    }

}
