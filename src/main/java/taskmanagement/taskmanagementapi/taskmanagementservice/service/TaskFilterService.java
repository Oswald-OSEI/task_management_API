package taskmanagement.taskmanagementapi.taskmanagementservice.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.taskmanagementservice.dto.TaskDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskManagementException;
import taskmanagement.taskmanagementapi.taskmanagementservice.mapper.TaskMapper;
import taskmanagement.taskmanagementapi.taskmanagementservice.repository.TaskRepository;

import java.time.LocalDate;

@Service
public class TaskFilterService {

    @Value("${jwt.secret}")
    private String secretString;

    private final TaskRepository taskRepository;

    private final TokenService tokenService;

    public TaskFilterService(TaskRepository taskRepository, TokenService tokenService, TokenService tokenService1) {
        this.taskRepository = taskRepository;
        this.tokenService = tokenService1;
    }

    public Page<TaskDto> filterTasksByStatus(String token, Status status, Pageable pageable) {
        try{
            long creatorId = tokenService.extractId(token, secretString);

            return taskRepository.findAllByCreator_IdAndStatus(creatorId, status, pageable)
                .map(TaskMapper::toTaskDto);}

        catch(Exception e){
            throw new TaskManagementException("task does not exist");
        }
    }

    public Page<TaskDto> filterTasksByPriority(String token, Priority priority, Pageable pageable) {
        try{
            long creatorId = tokenService.extractId(token, secretString);
            return taskRepository.findAllByPriorityAndCreator_Id(priority, creatorId, pageable)
                .map(TaskMapper::toTaskDto);}
        catch(Exception e){
            throw new TaskManagementException("task does not exist");
        }
    }

    public Page<TaskDto> filterTasksByDeadlineOnOrBefore(String token, LocalDate deadline, Pageable pageable) {
        try{
            long creatorId = tokenService.extractId(token, secretString);
            return taskRepository.findAllByDeadlineLessThanEqualAndCreator_Id(deadline, creatorId,pageable)
                .map(TaskMapper::toTaskDto);}
        catch(Exception e){
            throw new TaskManagementException("task does not exist");
        }
    }

    public Page<TaskDto> filterTasksByDeadlineOnOrAfter(String token, LocalDate deadline, Pageable pageable) {
        try{
            long creatorId = tokenService.extractId(token, secretString);
            return taskRepository.findAllByDeadlineGreaterThanEqualAndCreator_Id(deadline, creatorId, pageable)
                .map(TaskMapper::toTaskDto);}
        catch(Exception e){
            throw new TaskManagementException("task does not exist");
        }
    }

    public Page<TaskDto> filterTasksByDeadlineOn(String token, LocalDate deadline, Pageable pageable) {
        try{
            long creatorId = tokenService.extractId(token, secretString);
            return taskRepository.findAllByDeadlineAndCreator_Id(deadline, creatorId, pageable)
                .map(TaskMapper::toTaskDto);}
        catch(Exception e){
            throw new TaskManagementException("task does not exist");
        }
    }
}
