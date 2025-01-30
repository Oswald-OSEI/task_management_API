package taskmanagement.taskmanagementapi.userservice.dto;

import lombok.NonNull;

public record SignUpRequestDto(
        @NonNull String username,
        @NonNull String password
) {}
