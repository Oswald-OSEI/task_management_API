package taskmanagement.taskmanagementapi.common.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
        int statusCode,
        HttpStatus status,
        String message
) {}
