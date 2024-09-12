package io.codefresh.gradleexample.application.exceptions;

public class AuthorIdNotMatchWithAuthorTypeException extends RuntimeException {
    public AuthorIdNotMatchWithAuthorTypeException(String message) {
        super(message);
    }
}
