package taskmanagement.taskmanagementapi.common.dto;

public record ApiResponseDto<T>(
        int statusCode,
        String message,
        T data

) {}
