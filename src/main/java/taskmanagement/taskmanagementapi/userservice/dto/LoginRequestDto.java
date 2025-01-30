package taskmanagement.taskmanagementapi.userservice.dto;

import lombok.NonNull;

public record LoginRequestDto(
        @NonNull String username,
        @NonNull String password

) {}
