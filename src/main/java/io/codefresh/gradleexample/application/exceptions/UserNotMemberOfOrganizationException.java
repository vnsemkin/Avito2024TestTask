package io.codefresh.gradleexample.application.exceptions;

public class UserNotMemberOfOrganizationException extends RuntimeException {
    private static final String REASON = "reason: ";
    public UserNotMemberOfOrganizationException(String message) {
        super(REASON + message);
    }
}
