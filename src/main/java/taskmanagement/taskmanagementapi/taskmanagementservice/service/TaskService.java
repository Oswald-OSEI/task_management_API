package taskmanagement.taskmanagementapi.taskmanagementservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.taskmanagementservice.dto.TaskDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.entity.TaskEntity;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskCreationException;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskManagementException;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskOperationAuthorizationException;
import taskmanagement.taskmanagementapi.taskmanagementservice.mapper.TaskMapper;
import taskmanagement.taskmanagementapi.taskmanagementservice.repository.TaskRepository;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

import java.util.List;

import static taskmanagement.taskmanagementapi.taskmanagementservice.mapper.TaskMapper.*;

@Service
@Slf4j
public class TaskService {
    private final TokenService tokenService;
    @Value("${jwt.secret}")
    private String secretString;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TokenService tokenService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String addTask(List<TaskDto> taskDto,String token) {
        if(taskDto.isEmpty() || taskDto.size()>5 ){
            throw new TaskCreationException("Failed! You must add a minimum of 1 and maximum of 5");
        }
        else{
            try {
                long creatorId = tokenService.extractId(token, secretString);
                UserEntity user = userRepository.findById(creatorId).orElseThrow(() -> new TaskOperationAuthorizationException("user not authorized"));
                List<TaskEntity> tasks = toTaskEntityList(taskDto);
                tasks.forEach(task -> task.setCreator(user));
                taskRepository.saveAll(tasks);
                return "Tasks added successfully";
            }catch(Exception e){
                throw new TaskOperationAuthorizationException("user not authorized");
            }
        }
    }

    public Page<TaskDto> getAllTasks(Pageable pageable, String token) {
        try{ long creatorId = tokenService.extractId(token, secretString);
            return taskRepository.findAllByCreator_Id(pageable, creatorId).
                                map(TaskMapper::toTaskDto);}
        catch(Exception e){
            log.error("unable to retrieve user's tasks", e);
            throw new TaskOperationAuthorizationException("user not authorized");
        }
    }

    public TaskDto updateTask(String token, TaskDto taskDto) {
        try{
        long creatorId = tokenService.extractId(token, secretString);
        TaskEntity task = taskRepository.findByIdAndCreator_Id(taskDto.id(), creatorId).orElseThrow(()->new TaskManagementException("Task with not found"));
        task.setTitle(taskDto.title());
        task.setPriority(taskDto.priority());
        task.setStatus(taskDto.status());
        task.setDeadline(taskDto.deadline());
        return toTaskDto(taskRepository.save(task));}
        catch(Exception e){
            throw new TaskOperationAuthorizationException("user not authorized");
        }
    }

    public TaskDto getTask(long taskId, String token) {
        try{
            long creatorId = tokenService.extractId(token, secretString);
            return toTaskDto(taskRepository.findByIdAndCreator_Id(taskId, creatorId).orElseThrow(()->new TaskManagementException("Task not found")));
        }
        catch(Exception e){
            throw new TaskOperationAuthorizationException("user not authorized");
        }

    }

    public String deleteTask(long taskId, String token) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(()->new TaskManagementException("Task not found"));
        try{
            if(task.getCreator().getId()== tokenService.extractId(token, secretString)){
                taskRepository.delete((task));
                return "task deleted";
            }else{
                throw new TaskOperationAuthorizationException("you are not authorized to delete this task");
            }
        }catch (Exception e){
            throw new TaskOperationAuthorizationException("user not authorized");
        }
        
    }
}
