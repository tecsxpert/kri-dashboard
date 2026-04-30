package com.internship.tool.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.ContextConfiguration;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests JwtUtil via a minimal Spring context so the @Value("${jwt.secret}")
 * is injected exactly as it would be at runtime — no ReflectionTestUtils.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtUtilTest.TestConfig.class)
@TestPropertySource(properties = "jwt.secret=YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY3")
class JwtUtilTest {

    // Same secret used by @TestPropertySource — lets us build helper tokens
    private static final String TEST_SECRET =
            "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY3";

    @TestConfiguration
    static class TestConfig {
        @Bean
        JwtUtil jwtUtil() {
            return new JwtUtil();
        }
    }

    @Autowired
    JwtUtil jwtUtil;

    // ================================================================== //
    //  generateToken                                                      //
    // ================================================================== //

    @Test
    @DisplayName("generateToken: returns a non-null, non-blank token")
    void generateToken_returnsNonBlankToken() {
        String token = jwtUtil.generateToken("user@example.com");
        assertThat(token).isNotNull().isNotBlank();
    }

    // ================================================================== //
    //  extractUsername                                                    //
    // ================================================================== //

    @Test
    @DisplayName("extractUsername: returns the subject used when generating the token")
    void extractUsername_matchesInput() {
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(username);
        assertThat(jwtUtil.extractUsername(token)).isEqualTo(username);
    }

    // ================================================================== //
    //  extractAllClaims                                                   //
    // ================================================================== //

    @Test
    @DisplayName("extractAllClaims: subject matches and expiration is after issuedAt")
    void extractAllClaims_containsCorrectSubject() {
        String username = "claims@test.com";
        String token = jwtUtil.generateToken(username);

        Claims claims = jwtUtil.extractAllClaims(token);

        assertThat(claims.getSubject()).isEqualTo(username);
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
    }

    // ================================================================== //
    //  validateToken — valid                                              //
    // ================================================================== //

    @Test
    @DisplayName("validateToken: returns true for fresh token with matching username")
    void validateToken_validToken_returnsTrue() {
        String username = "valid@example.com";
        String token = jwtUtil.generateToken(username);
        assertThat(jwtUtil.validateToken(token, username)).isTrue();
    }

    // ================================================================== //
    //  validateToken — wrong username                                     //
    // ================================================================== //

    @Test
    @DisplayName("validateToken: returns false when username does not match token subject")
    void validateToken_wrongUsername_returnsFalse() {
        String token = jwtUtil.generateToken("correct@example.com");
        assertThat(jwtUtil.validateToken(token, "wrong@example.com")).isFalse();
    }

    // ================================================================== //
    //  validateToken — expired token                                      //
    // ================================================================== //

    @Test
    @DisplayName("validateToken: returns false for an already-expired token")
    void validateToken_expiredToken_returnsFalse() {
        String expired = buildExpiredToken("expired@example.com");

        boolean result = jwtUtil.validateToken(expired, "expired@example.com");

        assertThat(result).isFalse();
    }

    // ================================================================== //
    //  validateToken — malformed token                                    //
    // ================================================================== //

    @Test
    @DisplayName("validateToken: returns false for a malformed / random string token")
    void validateToken_malformedToken_returnsFalse() {
        boolean result = jwtUtil.validateToken("not.a.valid.jwt", "anyone@example.com");

        assertThat(result).isFalse();
    }

    // ------------------------------------------------------------------ //
    //  Private helper — build a pre-expired token using the same key     //
    // ------------------------------------------------------------------ //

    private String buildExpiredToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET));
        Date past = new Date(System.currentTimeMillis() - 60_000L);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(past.getTime() - 60_000L))
                .expiration(past)
                .signWith(key)
                .compact();
    }
}
