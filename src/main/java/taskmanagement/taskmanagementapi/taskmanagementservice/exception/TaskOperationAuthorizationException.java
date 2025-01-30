package taskmanagement.taskmanagementapi.taskmanagementservice.exception;

public class TaskOperationAuthorizationException extends RuntimeException {
    public TaskOperationAuthorizationException(String message) {
        super(message);
    }
}
