package taskmanagement.taskmanagementapi.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import taskmanagement.taskmanagementapi.userservice.dto.SignUpRequestDto;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.exceptions.SignUpException;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignUpService signUpService;

    private final String TEST_USERNAME = "testUser";
    private final String TEST_PASSWORD = "testPass";
    private final String TEST_ENCODED_PASSWORD = "encodedPassword";

    @Test
    void signUp_SuccessfulCase() {
        // Setup
        SignUpRequestDto request = new SignUpRequestDto(TEST_USERNAME, TEST_PASSWORD);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

        // Execute
        String result = signUpService.signUp(request);

        // Verify
        assertEquals("sign up successful", result);
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode(TEST_PASSWORD);
    }

    @Test
    void signUp_EmptyUsername_ThrowsException() {
        // Setup
        SignUpRequestDto request = new SignUpRequestDto("", TEST_PASSWORD);

        // Execute & Verify
        SignUpException exception = assertThrows(SignUpException.class,
                () -> signUpService.signUp(request));
        assertEquals("username or password cannot be empty", exception.getMessage());
    }

    @Test
    void signUp_EmptyPassword_ThrowsException() {
        // Setup
        SignUpRequestDto request = new SignUpRequestDto(TEST_USERNAME, "");

        // Execute & Verify
        SignUpException exception = assertThrows(SignUpException.class,
                () -> signUpService.signUp(request));
        assertEquals("username or password cannot be empty", exception.getMessage());
    }

    @Test
    void signUp_DuplicateUser_ThrowsException() {
        // Setup
        SignUpRequestDto request = new SignUpRequestDto(TEST_USERNAME, TEST_PASSWORD);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenThrow(new DataIntegrityViolationException(""));

        // Execute & Verify
        SignUpException exception = assertThrows(SignUpException.class,
                () -> signUpService.signUp(request));
        assertEquals("user already exists", exception.getMessage());
    }
}