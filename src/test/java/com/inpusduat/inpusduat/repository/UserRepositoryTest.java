package com.inpusduat.inpusduat.repository;

import com.inpusduat.inpusduat.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .role(User.Role.USER)
                .active(true)
                .build();
        entityManager.persistAndFlush(testUser);
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
        boolean exists = userRepository.existsByEmail("test@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailNotExists() {
        boolean exists = userRepository.existsByEmail("noexiste@example.com");
        assertThat(exists).isFalse();
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        boolean exists = userRepository.existsByUsername("testuser");
        assertThat(exists).isTrue();
    }
}