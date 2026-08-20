package com.taskmanager.enterprizetaskmanager.exceptions;

public class AuthorityNotFoundException extends RuntimeException{
    public AuthorityNotFoundException(String message) {
        super(message);
    }
}
