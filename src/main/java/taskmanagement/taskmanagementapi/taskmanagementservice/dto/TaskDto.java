package taskmanagement.taskmanagementapi.taskmanagementservice.dto;

import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;
import java.time.LocalDate;

public record TaskDto(
        Long id,

        String title,

        Priority priority,

        Status status,

        LocalDate deadline,

        Long creatorId
) {}
