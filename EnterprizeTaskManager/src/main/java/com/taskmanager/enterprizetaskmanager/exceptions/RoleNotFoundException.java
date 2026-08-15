package com.taskmanager.enterprizetaskmanager.exceptions;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message) {
        super(message);
    }
}
