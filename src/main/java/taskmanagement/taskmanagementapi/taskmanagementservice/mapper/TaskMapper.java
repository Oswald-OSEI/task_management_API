package taskmanagement.taskmanagementapi.taskmanagementservice.mapper;

import taskmanagement.taskmanagementapi.taskmanagementservice.dto.TaskDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.entity.TaskEntity;

import java.util.List;


public class TaskMapper {

    public static TaskDto toTaskDto(TaskEntity taskEntity) {
        return new TaskDto(
                taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getPriority(),
                taskEntity.getStatus(),
                taskEntity.getDeadline(),
                taskEntity.getCreator().getId()
        );
    }

    public static TaskEntity toTaskEntity(TaskDto taskDto) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDto.title());
        taskEntity.setPriority(taskDto.priority());
        taskEntity.setStatus(taskDto.status());
        taskEntity.setDeadline(taskDto.deadline());
        return taskEntity;
    }

    public static List<TaskEntity> toTaskEntityList(List<TaskDto> taskDtoList) {
        return taskDtoList.
                stream().
                map(TaskMapper::toTaskEntity).
                toList();
    }

    public static List<TaskDto> toTaskDtoList(List<TaskEntity> taskEntityList) {
        return taskEntityList.
                stream().
                map(TaskMapper::toTaskDto)
                .toList();
    }
}
