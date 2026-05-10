package com.inpusduat.inpusduat.repository;

import com.inpusduat.inpusduat.domain.Role;
import com.inpusduat.inpusduat.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.inpusduat.inpusduat.repository.RefreshTokenRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        mediaRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashedpassword");
        testUser.setRole(Role.USER);
        testUser.setActive(true);
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        mediaRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        Optional<User> found = userRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailNotExists() {
        Optional<User> found = userRepository.findByEmail("noexiste@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailNotExists() {
        assertThat(userRepository.existsByEmail("noexiste@example.com")).isFalse();
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        assertThat(userRepository.existsByUsername("testuser")).isTrue();
    }
}