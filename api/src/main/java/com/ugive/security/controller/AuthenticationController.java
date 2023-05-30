package com.ugive.security.controller;

import com.ugive.security.config.JwtConfig;
import com.ugive.security.dto.AuthRequest;
import com.ugive.security.dto.AuthResponse;
import com.ugive.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserDetailsService userDetailsService;

    private final JwtConfig jwtConfiguration;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getUserPassword() + jwtConfiguration.getPasswordSalt()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = tokenProvider.generateToken(userDetailsService.loadUserByUsername(request.getLogin()));

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .login(request.getLogin())
                        .token(token)
                        .build()
        );
    }
}
