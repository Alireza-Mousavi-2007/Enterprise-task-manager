package com.taskmanager.enterprizetaskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotFoundHandler(UserNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> roleNotFoundHandler(RoleNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> taskNotFoundHandler(TaskNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

}
