package com.example.rest_graphql_token.service;

import com.example.rest_graphql_token.dto.ApiResponse;
import com.example.rest_graphql_token.dto.LoginRequest;
import com.example.rest_graphql_token.dto.TokenResponse;
import com.example.rest_graphql_token.security.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 로그인 인증 및 토큰 생성
    public ApiResponse<TokenResponse> authenticateUser(LoginRequest loginRequest) {
        // 여기에 사용자 인증 로직 추가
        // 예를 들어 DB에서 username과 password 체크 후 인증

        // 인증 성공 시 액세스 토큰과 리프레시 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(loginRequest.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(loginRequest.getUsername());

        return new ApiResponse<>("성공",new TokenResponse(accessToken, refreshToken));
    }

    // 토큰 갱신 메서드
    public ApiResponse<TokenResponse> refreshToken(String incomingRefreshToken) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(incomingRefreshToken);
        String refreshToken = jsonNode.get("refreshToken").asText();

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtTokenProvider.generateToken(username);
            return new ApiResponse<>("성공", new TokenResponse(newAccessToken, refreshToken));
        } else {
            // 원인 출력
            System.out.println(jwtTokenProvider.validateToken(refreshToken));
            return new ApiResponse<>("만료된 토큰입니다.", new TokenResponse(null, null));
        }
    }
}