package com.birth.forumhub.modules.exception.usecase;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
