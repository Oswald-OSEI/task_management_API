package taskmanagement.taskmanagementapi.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import taskmanagement.taskmanagementapi.userservice.dto.AccessTokenRequest;
import taskmanagement.taskmanagementapi.userservice.dto.LoginRequestDto;
import taskmanagement.taskmanagementapi.userservice.dto.LoginResponseDto;
import taskmanagement.taskmanagementapi.userservice.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldReturnSuccessResponse() {
        // Setup
        LoginResponseDto expectedResponse = new LoginResponseDto(
                "kofi@hotmail.com",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        );
        when(authenticationService.login(any())).thenReturn(expectedResponse);

        // Execute
        var response = authenticationController.login(new LoginRequestDto("kofi@hotmail.com", "4Kofi"));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody().data());
    }

    @Test
    void refreshToken_ShouldReturnNewToken() {
        // Arrange
        AccessTokenRequest request = new AccessTokenRequest("refresh_token");
        when(authenticationService.getNewAccessToken("refresh_token")).thenReturn("new_token");

        // Act
        var response = authenticationController.refreshAccesssToken(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("new token created successfully", response.getBody().message());
        assertEquals("new_token", response.getBody().data());
    }
}