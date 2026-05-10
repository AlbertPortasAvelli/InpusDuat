package com.inpusduat.inpusduat.service;

import com.inpusduat.inpusduat.domain.RefreshToken;
import com.inpusduat.inpusduat.domain.Role;
import com.inpusduat.inpusduat.domain.User;
import com.inpusduat.inpusduat.dto.auth.AuthResponse;
import com.inpusduat.inpusduat.dto.auth.LoginRequest;
import com.inpusduat.inpusduat.dto.auth.RefreshRequest;
import com.inpusduat.inpusduat.dto.auth.RegisterRequest;
import com.inpusduat.inpusduat.exception.DuplicateResourceException;
import com.inpusduat.inpusduat.exception.UnauthorizedException;
import com.inpusduat.inpusduat.repository.RefreshTokenRepository;
import com.inpusduat.inpusduat.repository.UserRepository;
import com.inpusduat.inpusduat.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository; // NEW

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // NEW — 7 days in seconds
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 604800;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();

        User saved = userRepository.save(user);
        log.info("User registered: userId={}, email={}", saved.getId(), saved.getEmail());
        

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = createAndSaveRefreshToken(user); // NEW

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken()) // NEW — was null
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String accessToken = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        log.info("User logged in: userId={}, email={}", user.getId(), user.getEmail()); 
        RefreshToken refreshToken = createAndSaveRefreshToken(user);            

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken()) // NEW — was null
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000)
                .build();
    }

    // NEW — full method
    public AuthResponse refresh(RefreshRequest request) {
    RefreshToken stored = refreshTokenRepository.findByToken(request.getRefreshToken())
            .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

    if (Boolean.TRUE.equals(stored.getRevoked())) {          // ← was stored.isRevoked()
        throw new UnauthorizedException("Refresh token has been revoked");
    }
    if (stored.isExpired()) {                                 // ← was isBefore(LocalTime.now())
        throw new UnauthorizedException("Refresh token has expired");
    }

    stored.setRevoked(true);
    refreshTokenRepository.save(stored);

    User user = stored.getUser();
    log.info("Token refreshed: userId={}", user.getId());
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    String newAccessToken = jwtService.generateToken(userDetails);
    RefreshToken newRefreshToken = createAndSaveRefreshToken(user);

    return AuthResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken.getToken())
            .tokenType("Bearer")
            .expiresIn(jwtExpiration / 1000)
            .build();
    }

    // NEW — shared helper used by register, login, and refresh
    private RefreshToken createAndSaveRefreshToken(User user) {
        refreshTokenRepository.deleteByUserAndRevokedTrue(user);
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS)) // ← was LocalTime
                .revoked(false)
                .build();
        return refreshTokenRepository.save(token);
    }
}