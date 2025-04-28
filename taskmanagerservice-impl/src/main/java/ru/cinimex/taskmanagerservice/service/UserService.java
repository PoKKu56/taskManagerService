package ru.cinimex.taskmanagerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinimex.taskmanagerservice.domain.EmailMessage;
import ru.cinimex.taskmanagerservice.domain.TempCodeEntity;
import ru.cinimex.taskmanagerservice.domain.UserEntity;
import ru.cinimex.taskmanagerservice.dto.*;
import ru.cinimex.taskmanagerservice.mapper.UserMapper;
import ru.cinimex.taskmanagerservice.repository.TempCodeRepository;
import ru.cinimex.taskmanagerservice.repository.UserRepository;
import ru.cinimex.taskmanagerservice.util.CheckCodeError;
import ru.cinimex.taskmanagerservice.util.RegisterError;
import ru.cinimex.taskmanagerservice.util.UnknownUserError;
import ru.cinimex.taskmanagerservice.util.loginError;

import java.util.Date;
import java.util.List;
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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponse convertAndSaveUser(RegisterRequest registerRequest){

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new RegisterError("Пользователь с таким логином уже существует");
        }
        else if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new RegisterError("Пользователь с такой почтой уже существует");
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

        return new RegisterResponse("Успешная регистрация", userEntity.getId());
    }

    @Transactional
    public RegisterResponse checkEmailCode(RegisterConfirmationRequest registerConfirmationRequest){
        UserEntity userEntity = userRepository.findById(registerConfirmationRequest.getId()).orElseThrow(() ->
                new RegisterError("Пользователь с таким id не найден"));

        if(!userEntity.isActive()){
            TempCodeEntity tempCodeEntity = tempCodeRepository.findByUser(userEntity).orElseThrow(() ->
                    new CheckCodeError("Проверочный код не найден на сервере"));

            if (tempCodeEntity.getCode().equals(registerConfirmationRequest.getCode())){

                userEntity.setActive(true);
                userEntity.setUpdatedAt(new Date());
                userRepository.save(userEntity);
                tempCodeRepository.delete(tempCodeEntity);
                return new RegisterResponse("Успешная регистрация",
                        userEntity.getId());
            }

            else{
                throw new RegisterError("Неверный код");
            }
        }
        throw new RegisterError("Пользователь уже активировал аккаунт");
    }

    public CurrentUserResponse getCurrentUser(){

        checkJwtToken();

        UserEntity user = userRepository
                .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UnknownUserError("Такого пользователя не существует"));

        if (!user.getRole().equals("ROLE_USER") && !user.getRole().equals("ROLE_ADMIN")){
            throw new loginError("Отказ в доступе");
        }

        return new CurrentUserResponse(user.getUsername(), user.getEmail(),
                user.getRole());
    }

    public CurrentUserResponse getCurrentUserById(UUID id){

        checkJwtToken();

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UnknownUserError("Такого пользователя не существует") );

        checkTokenRoles(List.of("ROLE_TECH", "ROLE_ADMIN"));

        return new CurrentUserResponse(user.getUsername(), user.getEmail(),
                user.getRole());
    }

    public Authentication loginUser(AuthRequest authRequest){
        UserEntity user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(
                () -> new loginError("Пользователь с таким логином не найден")
        );

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())){
            throw new loginError("Неверный логин/пароль");
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
    }

    public TokenResponse createTechToken(CreateTechTokenRequest createTechTokenRequest){

        checkJwtToken();

        UserEntity user = userRepository
                .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UnknownUserError("Такого пользователя не существует"));

        checkTokenRoles(List.of("ROLE_ADMIN"));

        return new TokenResponse(jwtService.generateTechToken(createTechTokenRequest.getExpiredDate()));
    }


    private void checkJwtToken(){

        if (SecurityContextHolder.getContext() == null){
            throw new UnknownUserError("Отсутствует JWT-токен");
        }

    }

    private void checkTokenRoles(List<String> roles){

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(roles::contains))
            throw new loginError("Отказано в доступе");
    }

}
