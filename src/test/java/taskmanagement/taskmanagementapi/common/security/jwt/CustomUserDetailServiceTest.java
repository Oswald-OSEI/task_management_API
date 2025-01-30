package taskmanagement.taskmanagementapi.common.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.exceptions.InvalidUserCredentialException;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// CustomUserDetailsServiceTest.java
@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_SuccessfulCase() {
        // Setup
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Execute
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Verify
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertInstanceOf(CustomUserDetails.class, result);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // Setup
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(InvalidUserCredentialException.class,
                () -> userDetailsService.loadUserByUsername("nonexistent"));
    }
}
