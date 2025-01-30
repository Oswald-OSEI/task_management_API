package taskmanagement.taskmanagementapi.common.exceptions;


public record TokenValidationExceptionWrapper(boolean isValid, Exception exception) {

}
