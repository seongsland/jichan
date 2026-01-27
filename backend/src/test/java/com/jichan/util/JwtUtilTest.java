package com.jichan.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "this-is-a-very-long-secret-key-for-testing-purpose-only");
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpiration", 3600000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpiration", 86400000L);
    }

    @Test
    void generateAndValidateAccessToken() {
        Long userId = 1L;
        String token = jwtUtil.generateAccessToken(userId);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(userId, jwtUtil.getUserIdFromToken(token));
    }

    @Test
    void generateAndValidateRefreshToken() {
        Long userId = 1L;
        String token = jwtUtil.generateRefreshToken(userId);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(userId, jwtUtil.getUserIdFromToken(token));
    }

    @Test
    void invalidToken() {
        assertFalse(jwtUtil.validateToken("invalid-token"));
    }
}
