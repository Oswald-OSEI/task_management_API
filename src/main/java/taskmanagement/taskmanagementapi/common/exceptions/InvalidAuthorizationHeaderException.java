package taskmanagement.taskmanagementapi.common.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException{
    public InvalidAuthorizationHeaderException(String message) {super(message);}
}
