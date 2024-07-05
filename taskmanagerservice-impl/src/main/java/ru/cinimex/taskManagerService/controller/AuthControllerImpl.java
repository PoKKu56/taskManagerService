package ru.cinimex.taskManagerService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.controller.AuthController;
import ru.cinimex.dto.AuthRequest;
import ru.cinimex.dto.TokenResponse;
import ru.cinimex.taskManagerService.service.JwtService;
import ru.cinimex.taskManagerService.service.UserDetailService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @Override
    public ResponseEntity<TokenResponse> login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(authentication)));
    }
}
