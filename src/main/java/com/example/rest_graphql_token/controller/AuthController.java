package com.example.rest_graphql_token.controller;

import com.example.rest_graphql_token.dto.ApiResponse;
import com.example.rest_graphql_token.dto.LoginRequest;
import com.example.rest_graphql_token.dto.TokenResponse;
import com.example.rest_graphql_token.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 로그인 API
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    // 토큰 갱신 API
    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody String refreshToken) throws JsonProcessingException {
        return authService.refreshToken(refreshToken);
    }
}
