package ru.cinimex.taskmanagerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinimex.taskmanagerservice.domain.EmailMessage;
import ru.cinimex.taskmanagerservice.domain.TempCodeEntity;
import ru.cinimex.taskmanagerservice.domain.UserEntity;
import ru.cinimex.taskmanagerservice.dto.CurrentUserResponse;
import ru.cinimex.taskmanagerservice.dto.RegisterConfirmationRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterResponse;
import ru.cinimex.taskmanagerservice.mapper.UserMapper;
import ru.cinimex.taskmanagerservice.repository.TempCodeRepository;
import ru.cinimex.taskmanagerservice.repository.UserRepository;
import ru.cinimex.taskmanagerservice.util.RegisterError;
import ru.cinimex.taskmanagerservice.util.UnknowUserError;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TempCodeRepository tempCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    private final ProducerVerificationService producerVerificationService;

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public ResponseEntity<?> convertAndSaveUser(RegisterRequest registerRequest){

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("Пользователь уже существует");
        }
        else if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body("Пользователь с таким email существует");
        }
        UserEntity userEntity = userMapper.userDtoToEntity(registerRequest);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        TempCodeEntity tempCodeEntity = new TempCodeEntity();
        tempCodeEntity.setUser(userEntity);
        tempCodeEntity.setCode(String.valueOf(random.nextInt(900000) + 100000));


        producerVerificationService.sendUserMessage(new EmailMessage(userEntity.getEmail(),
                "Подтверждение почты", "Ваш код подтверждения " + tempCodeEntity.getCode()));


        userRepository.save(userEntity);
        tempCodeRepository.save(tempCodeEntity);

        return ResponseEntity.ok(new RegisterResponse("Успешная регистрация",
                userEntity.getId()));
    }

    @Transactional
    public ResponseEntity<?> checkEmailCode(RegisterConfirmationRequest registerConfirmationRequest){
        Optional<UserEntity> userEntity = userRepository.findById(registerConfirmationRequest.getId());
        if(userEntity.isPresent() && !userEntity.get().isActive()){
            Optional<TempCodeEntity> tempCodeEntity = tempCodeRepository.findByUser(userEntity);
            if (tempCodeEntity.isPresent() &&
                    tempCodeEntity.get().getCode().equals(registerConfirmationRequest.getCode())){
                userEntity.get().setActive(true);
                userEntity.get().setUpdatedAt(new Date());
                userRepository.save(userEntity.get());
                return ResponseEntity.ok(new RegisterResponse("Успешная регистрация",
                        userEntity.get().getId()));
            }
            else{
                return ResponseEntity.status(HttpStatusCode.valueOf(422)).body("Неверный код");
            }
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("Пользователь уже существует");
    }

    public ResponseEntity<CurrentUserResponse> getCurrentUser(){

        Optional<UserEntity> user = userRepository
                .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isEmpty()){
            throw new UnknowUserError("Такого пользователя не существует");
        }

        return ResponseEntity.ok(new CurrentUserResponse(user.get().getUsername(), user.get().getEmail(),
                user.get().getRole()));
    }

    public ResponseEntity<CurrentUserResponse> getCurrentUserById(UUID id){

        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new UnknowUserError("Такого пользователя не существует");
        }

        return ResponseEntity.ok(new CurrentUserResponse(user.get().getUsername(), user.get().getEmail(),
                user.get().getRole()));
    }
}
