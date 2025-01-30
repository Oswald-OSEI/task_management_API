package taskmanagement.taskmanagementapi.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import taskmanagement.taskmanagementapi.userservice.dto.SignUpRequestDto;
import taskmanagement.taskmanagementapi.userservice.service.SignUpService;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RegistrationControllerTest {
    @Mock
    private SignUpService signUpService;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ShouldReturnSuccessResponse() {
        // Arrange
        SignUpRequestDto signUpRequest = new SignUpRequestDto("Kofi Wusu", "@Kofiwusu");
        when(signUpService.signUp(signUpRequest)).thenReturn("user_id");

        // Act
        var response = registrationController.signUp(signUpRequest);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getBody().statusCode());
        assertEquals("Sign up successful", response.getBody().message());
        assertEquals("user_id", response.getBody().data());
    }
}

