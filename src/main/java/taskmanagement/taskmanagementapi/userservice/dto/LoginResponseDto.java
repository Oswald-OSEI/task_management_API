package taskmanagement.taskmanagementapi.userservice.dto;

public record LoginResponseDto(
        String username,
        String accessToken,
        String refreshToken
) {}
