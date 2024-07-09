package ru.cinimex.taskmanagerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinimex.taskmanagerservice.domain.TempCodeEntity;
import ru.cinimex.taskmanagerservice.domain.UserEntity;
import ru.cinimex.taskmanagerservice.dto.RegisterConfirmationRequest;
import ru.cinimex.taskmanagerservice.dto.RegisterRequest;
import ru.cinimex.taskmanagerservice.mapper.UserMapper;
import ru.cinimex.taskmanagerservice.repository.TempCodeRepository;
import ru.cinimex.taskmanagerservice.repository.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TempCodeRepository tempCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
    public void convertAndSaveUser(RegisterRequest registerRequest){
        Random random = new Random();

        UserEntity userEntity = userMapper.userDtoToEntity(registerRequest);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        System.out.println(userEntity);

        TempCodeEntity tempCodeEntity = new TempCodeEntity();
        tempCodeEntity.setUser(userEntity);
        tempCodeEntity.setCode(String.valueOf(random.nextInt(900000) + 100000));

        userRepository.save(userEntity);
        tempCodeRepository.save(tempCodeEntity);
    }

    @Transactional
    public void checkEmailCode(RegisterConfirmationRequest registerConfirmationRequest){
        Optional<UserEntity> userEntity = userRepository.findById(registerConfirmationRequest.getId());
        System.out.println(userEntity);
        if(userEntity.isPresent() && !userEntity.get().isActive()){
            Optional<TempCodeEntity> tempCodeEntity = tempCodeRepository.findByUser(userEntity);

            if (tempCodeEntity.isPresent() &&
                    tempCodeEntity.get().getCode().equals(registerConfirmationRequest.getCode())){
                userEntity.get().setActive(true);
                userEntity.get().setUpdatedAt(new Date());
                userRepository.save(userEntity.get());
            }
        }
    }

    public Optional<UserEntity> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserEntity> userEntity = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

            userEntity = userRepository.findByUsername(jwtService.executeUserName((String) token.getCredentials()));
        }

        return userEntity;
    }

}
