package taskmanagement.taskmanagementapi.userservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskmanagement.taskmanagementapi.common.dto.ApiResponseDto;
import taskmanagement.taskmanagementapi.userservice.dto.SignUpRequestDto;
import taskmanagement.taskmanagementapi.userservice.service.SignUpService;

@RestController
@RequestMapping("v1/task-management/user")
public class RegistrationController {
    private final SignUpService signUpService;

    public RegistrationController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("sign-up")
    public ResponseEntity<ApiResponseDto<?>> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDto<>(HttpStatus.CREATED.value(),
                "Sign up successful",
                signUpService.signUp(signUpRequestDto)));
    }

}
