package org.example.gymcrm.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.gymcrm.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class JwtServiceTest {

  private JwtService jwtService;
  private Key key;

  private static final String SECRET_KEY =
      "yourbase64encodedsecretkeyyourbase64encodedsecretkey";
  private static final long JWT_LIFETIME_MILLIS = 60000;

  @Mock private UserDetails userDetails;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    jwtService = new JwtService();

    jwtService.setSECRET_KEY(SECRET_KEY);
    jwtService.setJwtLifetime(java.time.Duration.ofMillis(JWT_LIFETIME_MILLIS));

    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    key = Keys.hmacShaKeyFor(keyBytes);

    when(userDetails.getUsername()).thenReturn("testUser");
  }

  @Test
  void testGenerateToken() {
    String token = jwtService.generateToken(userDetails);
    assertNotNull(token);
  }

  @Test
  void testExtractUsername() {
    String token = jwtService.generateToken(userDetails);
    String username = jwtService.extractUsername(token);
    assertEquals("testUser", username);
  }

  @Test
  void testIsTokenValid_ValidToken() {
    String token = jwtService.generateToken(userDetails);
    assertTrue(jwtService.isTokenValid(token, userDetails));
  }

  @Test
  void testIsTokenValid_ExpiredToken() {
    Map<String, Object> claims = new HashMap<>();
    String expiredToken =
        Jwts.builder()
            .setClaims(claims)
            .setSubject("testUser")
            .setIssuedAt(new Date(System.currentTimeMillis() - 10000)) // 10 sec ago
            .setExpiration(new Date(System.currentTimeMillis() - 5000)) // Expired 5 sec ago
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    assertFalse(jwtService.isTokenValid(expiredToken, userDetails));
  }
}
