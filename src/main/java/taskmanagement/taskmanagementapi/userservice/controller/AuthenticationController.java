package taskmanagement.taskmanagementapi.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskmanagement.taskmanagementapi.common.dto.ApiResponseDto;
import taskmanagement.taskmanagementapi.userservice.dto.AccessTokenRequest;
import taskmanagement.taskmanagementapi.userservice.dto.LoginRequestDto;
import taskmanagement.taskmanagementapi.userservice.service.AuthenticationService;

@RestController
@RequestMapping("v1/task-management/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponseDto<?>> login(@RequestBody LoginRequestDto loginRequest){
        return ResponseEntity.ok(
                new ApiResponseDto<>(
                        HttpStatus.OK.value(),
                        "login in successful",
                        authenticationService.login(loginRequest)
                )
        );
    }
    @PostMapping("token/refresh")
    public ResponseEntity<ApiResponseDto<?>> refreshAccesssToken(@RequestBody AccessTokenRequest request){
        return ResponseEntity.ok(
                new ApiResponseDto<>(
                        HttpStatus.OK.value(),
                        "new token created successfully",
                        authenticationService.getNewAccessToken(request.getRefreshToken())
                )
        );
    }

}
