package com.inpusduat.inpusduat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inpusduat.inpusduat.repository.MediaRepository;
import com.inpusduat.inpusduat.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import com.inpusduat.inpusduat.repository.RefreshTokenRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired private MediaRepository mediaRepository;
    @Autowired private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        mediaRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        mediaRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void register_shouldReturnToken() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "username", "testuser",
            "email", "test@test.com",
            "password", "password123"
        ));
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void login_withValidCredentials_shouldReturnToken() throws Exception {
        String registerBody = objectMapper.writeValueAsString(Map.of(
            "username", "loginuser",
            "email", "login@test.com",
            "password", "password123"
        ));
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody));

        String loginBody = objectMapper.writeValueAsString(Map.of(
            "email", "login@test.com",
            "password", "password123"
        ));
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void login_withWrongPassword_shouldReturn403() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
            "email", "noexiste@test.com",
            "password", "wrongpass"
        ));
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().is4xxClientError());
    }
}