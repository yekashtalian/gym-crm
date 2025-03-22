package org.example.gymcrm.security;

import org.example.gymcrm.security.service.JwtBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtBlacklistServiceTest {
    private JwtBlacklistService jwtBlacklistService;

    @BeforeEach
    void setUp() {
        jwtBlacklistService = new JwtBlacklistService();
    }

    @Test
    void testBlacklistToken() {
        String token = "sampleToken";
        jwtBlacklistService.blacklistToken(token);
        assertTrue(jwtBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    void testTokenNotBlacklisted() {
        assertFalse(jwtBlacklistService.isTokenBlacklisted("unknownToken"));
    }
}

