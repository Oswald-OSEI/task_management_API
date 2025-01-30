package taskmanagement.taskmanagementapi.common.security.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

// JwtAuthenticationEntryPointTest.java
class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint entryPoint;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthenticationEntryPoint();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void commence_SetsUnauthorizedError() throws IOException {
        // Setup
        AuthenticationException authException = new AuthenticationException("Unauthorized") {};

        // Execute
        entryPoint.commence(request, response, authException);

        // Verify
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Unauthorized", response.getErrorMessage());
    }
}