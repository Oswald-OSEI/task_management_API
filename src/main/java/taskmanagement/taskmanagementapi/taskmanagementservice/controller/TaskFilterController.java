package taskmanagement.taskmanagementapi.taskmanagementservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.taskmanagementapi.common.dto.ApiResponseDto;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;
import taskmanagement.taskmanagementapi.taskmanagementservice.service.TaskFilterService;

import java.time.LocalDate;

@RestController
@RequestMapping("v1/task-management/filter")
public class TaskFilterController {
    private final TaskFilterService taskFilterService;

    public TaskFilterController(TaskFilterService taskFilterService, TokenService tokenService) {
        this.taskFilterService = taskFilterService;
    }

    @GetMapping("status/{status}")
    public ResponseEntity<ApiResponseDto<?>> filterTaskByStatus(@PathVariable Status status, @RequestHeader("Authorization") String token, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "retrieved successfully",
                taskFilterService.filterTasksByStatus(token, status, pageable)
                ));
    }

    @GetMapping("priority/{priority}")
    public ResponseEntity<ApiResponseDto<?>> filterTaskByPriority(@PathVariable Priority priority, @RequestHeader("Authorization") String token, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "retrieved successfully",
                taskFilterService.filterTasksByPriority(token, priority, pageable)
        ));
    }

    @GetMapping("/on-before/{deadline}")
    public ResponseEntity<ApiResponseDto<?>> filterByDeadlineOnOrBefore(@PathVariable LocalDate deadline, @RequestHeader("Authorization") String token, Pageable pageable){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "retrieved successfully",
                taskFilterService.filterTasksByDeadlineOnOrBefore(token, deadline, pageable )
        ));
    }

    @GetMapping("/on-after/{deadline}")
    public ResponseEntity<ApiResponseDto<?>> filterByDeadlineOnOrAfter(@PathVariable LocalDate deadline, @RequestHeader("Authorization") String token, Pageable pageable){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "retrieved successfully",
                taskFilterService.filterTasksByDeadlineOnOrAfter(token, deadline, pageable )
        ));
    }

    @GetMapping("/on/{deadline}")
    public ResponseEntity<ApiResponseDto<?>> filterByDeadlineOn(@PathVariable LocalDate deadline, @RequestHeader("Authorization") String token, Pageable pageable){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "retrieved successfully",
                taskFilterService.filterTasksByDeadlineOn(token, deadline, pageable )
        ));
    }
}
