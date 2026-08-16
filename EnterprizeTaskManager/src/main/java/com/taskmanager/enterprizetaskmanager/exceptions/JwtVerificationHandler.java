package com.taskmanager.enterprizetaskmanager.exceptions;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

public class JwtVerificationHandler extends AuthenticationException {
    public JwtVerificationHandler(@Nullable String msg) {
        super(msg);
    }
}
