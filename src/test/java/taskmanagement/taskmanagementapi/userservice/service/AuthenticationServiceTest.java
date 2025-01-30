package taskmanagement.taskmanagementapi.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import taskmanagement.taskmanagementapi.common.exceptions.InvalidAuthorizationHeaderException;
import taskmanagement.taskmanagementapi.common.exceptions.TokenValidationExceptionWrapper;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.userservice.dto.LoginRequestDto;
import taskmanagement.taskmanagementapi.userservice.dto.LoginResponseDto;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.exceptions.InvalidUserCredentialException;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final String TEST_USERNAME = "testUser";
    private final String TEST_PASSWORD = "testPass";
    private final String TEST_ENCODED_PASSWORD = "encodedPassword";
    private final Long TEST_USER_ID = 1L;
    private final String TEST_ACCESS_TOKEN = "access-token";
    private final String TEST_REFRESH_TOKEN = "refresh-token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationService, "refreshSecretString", "test-refresh-secret");
    }

    @Test
    void login_SuccessfulCase() {
        // Setup
        LoginRequestDto loginRequest = new LoginRequestDto(TEST_USERNAME, TEST_PASSWORD);
        UserEntity user = new UserEntity();
        user.setId(TEST_USER_ID);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_ENCODED_PASSWORD);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(true);
        when(tokenService.generateAccessToken(TEST_USER_ID, TEST_USERNAME)).thenReturn(TEST_ACCESS_TOKEN);
        when(tokenService.generateRefreshToken(TEST_USER_ID, TEST_USERNAME)).thenReturn(TEST_REFRESH_TOKEN);

        // Execute
        LoginResponseDto response = authenticationService.login(loginRequest);

        // Verify
        assertNotNull(response);
        assertEquals(TEST_USERNAME, response.username());
        assertEquals(TEST_ACCESS_TOKEN, response.accessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.refreshToken());
    }

    @Test
    void login_InvalidUsername_ThrowsException() {
        // Setup
        LoginRequestDto loginRequest = new LoginRequestDto(TEST_USERNAME, TEST_PASSWORD);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(InvalidUserCredentialException.class,
                () -> authenticationService.login(loginRequest));
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        // Setup
        LoginRequestDto loginRequest = new LoginRequestDto(TEST_USERNAME, TEST_PASSWORD);
        UserEntity user = new UserEntity();
        user.setPassword(TEST_ENCODED_PASSWORD);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(false);

        // Execute & Verify
        assertThrows(InvalidUserCredentialException.class,
                () -> authenticationService.login(loginRequest));
    }

    @Test
    void getNewAccessToken_SuccessfulCase() {
        // Setup
        TokenValidationExceptionWrapper validationResult = new TokenValidationExceptionWrapper(true, null);
        when(tokenService.validateToken(TEST_REFRESH_TOKEN, "test-refresh-secret"))
                .thenReturn(validationResult);
        when(tokenService.extractId(TEST_REFRESH_TOKEN, "test-refresh-secret"))
                .thenReturn(TEST_USER_ID);
        when(tokenService.extractUsername(TEST_REFRESH_TOKEN, "test-refresh-secret"))
                .thenReturn(TEST_USERNAME);
        when(tokenService.generateAccessToken(TEST_USER_ID, TEST_USERNAME))
                .thenReturn(TEST_ACCESS_TOKEN);

        // Execute
        String newAccessToken = authenticationService.getNewAccessToken(TEST_REFRESH_TOKEN);

        // Verify
        assertEquals(TEST_ACCESS_TOKEN, newAccessToken);
    }

    @Test
    void getNewAccessToken_InvalidToken_ThrowsException() {
        // Setup
        TokenValidationExceptionWrapper validationResult = new TokenValidationExceptionWrapper(false, null);
        when(tokenService.validateToken(TEST_REFRESH_TOKEN, "test-refresh-secret"))
                .thenReturn(validationResult);

        // Execute & Verify
        assertThrows(InvalidAuthorizationHeaderException.class,
                () -> authenticationService.getNewAccessToken(TEST_REFRESH_TOKEN));
    }
}
