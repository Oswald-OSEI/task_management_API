package taskmanagement.taskmanagementapi.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import taskmanagement.taskmanagementapi.common.exceptions.InvalidAuthorizationHeaderException;
import taskmanagement.taskmanagementapi.common.exceptions.TokenValidationExceptionWrapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// JwtAuthFilterTest.java
@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        ReflectionTestUtils.setField(jwtAuthFilter, "secretString", "testSecretKeyWithAtLeast32Characters!!");
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // Setup
        String token = "valid.test.token";
        request.addHeader("Authorization", "Bearer " + token);

        TokenValidationExceptionWrapper validationResult = new TokenValidationExceptionWrapper(true, null);
        when(tokenService.validateToken(token, jwtAuthFilter.secretString)).thenReturn(validationResult);
        when(tokenService.extractUsername(token, jwtAuthFilter.secretString)).thenReturn("testUser");

        UserDetails userDetails = new CustomUserDetails(1L, "testUser", "password");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        // Execute
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verify
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_InvalidToken_ThrowsException() {
        // Setup
        String token = "invalid.test.token";
        request.addHeader("Authorization", "Bearer " + token);

        TokenValidationExceptionWrapper validationResult = new TokenValidationExceptionWrapper(false, new InvalidAuthorizationHeaderException("invalid token"));
        when(tokenService.validateToken(token, jwtAuthFilter.secretString)).thenReturn(validationResult);

        // Execute & Verify
        assertThrows(InvalidAuthorizationHeaderException.class,
                () -> jwtAuthFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    void shouldNotFilter_PathsToSkip_ReturnsTrue() {
        // Setup
        request.setServletPath("/v1/task-management/auth/login");

        // Execute & Verify
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        request.setServletPath("/v1/task-management/user/sign-up");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        request.setServletPath("/v1/task-management/auth/token/refresh");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));
    }

    @Test
    void shouldNotFilter_ProtectedPath_ReturnsFalse() {
        // Setup
        request.setServletPath("/v1/task-management/tasks");

        // Execute & Verify
        assertFalse(jwtAuthFilter.shouldNotFilter(request));
    }
}