package io.codefresh.gradleexample.application.exceptions;

public class AuthorIdNotMatchWithAuthorTypeException extends RuntimeException {
    private static final String REASON = "reason: ";
    public AuthorIdNotMatchWithAuthorTypeException(String message) {
        super(REASON + message);
    }
}
