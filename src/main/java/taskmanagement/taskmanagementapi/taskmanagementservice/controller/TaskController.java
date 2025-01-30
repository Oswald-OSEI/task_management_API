package taskmanagement.taskmanagementapi.taskmanagementservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.taskmanagementapi.common.dto.ApiResponseDto;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.taskmanagementservice.dto.TaskDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("v1/task-management")
public class TaskController {
    @Value("${jwt.secret}")
    private String secretString;

    private final TaskService taskService;


    public TaskController(TaskService taskService, TokenService tokenService) {
        this.taskService = taskService;
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<?>> createTask(@RequestBody List<TaskDto> tasks, @RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.CREATED).
                                body(new ApiResponseDto<>(
                                        HttpStatus.CREATED.value(),
                                        "Successful",
                                        taskService.addTask(tasks, token)
                                ));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDto<?>> updateTask(@RequestBody TaskDto task, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "successful",
                taskService.updateTask(token, task)
        ));
    }

    @GetMapping("/all-tasks")
    public ResponseEntity<ApiResponseDto<?>> getAllTasks(@RequestHeader("Authorization") String token, Pageable pageable){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "Successful",
                taskService.getAllTasks(pageable, token)
        ));
    }

    @GetMapping("/task/{Id}")
    public ResponseEntity<ApiResponseDto<?>> getTaskById(@PathVariable("Id") long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "Successful",
                taskService.getTask(id, token))
        );
    }

    @DeleteMapping("/task/delete/{Id}")
    public ResponseEntity<ApiResponseDto<?>> deleteTask(@PathVariable("Id") long taskId, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK.value(),
                "Deleted Successfully",
                taskService.deleteTask(taskId, token))
        );
    }
}
