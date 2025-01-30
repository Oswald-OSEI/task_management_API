package taskmanagement.taskmanagementapi.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import taskmanagement.taskmanagementapi.common.dto.ErrorResponseDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskCreationException;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskManagementException;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskOperationAuthorizationException;
import taskmanagement.taskmanagementapi.userservice.exceptions.InvalidUserCredentialException;
import taskmanagement.taskmanagementapi.userservice.exceptions.SignUpException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAuthorizationHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidHeaderException(InvalidAuthorizationHeaderException e) {
       return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUserCredentialException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidUserCredentialException(InvalidUserCredentialException e) {
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(SignUpException e) {
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskOperationAuthorizationException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskOperationAuthorizationException(TaskOperationAuthorizationException e) {
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TaskCreationException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskCreationException(TaskCreationException e) {
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskManagementException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskManagementException(TaskManagementException e) {
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
