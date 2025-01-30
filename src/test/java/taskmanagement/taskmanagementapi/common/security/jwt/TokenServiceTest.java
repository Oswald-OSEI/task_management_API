package taskmanagement.taskmanagementapi.common.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import taskmanagement.taskmanagementapi.common.exceptions.TokenValidationExceptionWrapper;

import static org.junit.jupiter.api.Assertions.*;

// TokenServiceTest.java
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private final String TEST_SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private final String TEST_REFRESH_SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secretString", TEST_SECRET);
        ReflectionTestUtils.setField(tokenService, "refreshSecretString", TEST_REFRESH_SECRET);
    }

    @Test
    void generateAccessToken_SuccessfulCase() {
        // Execute
        String token = tokenService.generateAccessToken(1L, "testUser");

        // Verify
        assertNotNull(token);
        TokenValidationExceptionWrapper validation = tokenService.validateToken(token, TEST_SECRET);
        assertTrue(validation.isValid());
        assertEquals("testUser", tokenService.extractUsername(token, TEST_SECRET));
        assertEquals(1L, tokenService.extractId(token, TEST_SECRET));
    }

    @Test
    void generateRefreshToken_SuccessfulCase() {
        // Execute
        String token = tokenService.generateRefreshToken(1L, "testUser");

        // Verify
        assertNotNull(token);
        TokenValidationExceptionWrapper validation = tokenService.validateToken(token, TEST_REFRESH_SECRET);
        assertTrue(validation.isValid());
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // Setup
        String token = tokenService.generateAccessToken(1L, "testUser");

        // Execute
        TokenValidationExceptionWrapper result = tokenService.validateToken(token, TEST_SECRET);

        // Verify
        assertTrue(result.isValid());
        assertNull(result.exception());
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // Execute
        TokenValidationExceptionWrapper result = tokenService.validateToken("invalid.token.here", TEST_SECRET);

        // Verify
        assertFalse(result.isValid());
        assertNotNull(result.exception());
    }

    @Test
    void validateToken_ExpiredToken_ReturnsFalse() throws Exception {
        // This test would need a special token generator that creates expired tokens
        // For now we'll just use an expired token string
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImV4cCI6MTU3NjgwMDAwMH0";

        // Execute
        TokenValidationExceptionWrapper result = tokenService.validateToken(expiredToken, TEST_SECRET);

        // Verify
        assertFalse(result.isValid());
        assertNotNull(result.exception());
    }
}
