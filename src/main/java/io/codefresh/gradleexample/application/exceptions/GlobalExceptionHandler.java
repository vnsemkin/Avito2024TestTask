package io.codefresh.gradleexample.application.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage());
    }

    @ExceptionHandler(UserNotMemberOfOrganizationException.class)
    public ResponseEntity<?> handleUserNotMemberOfOrganization(UserNotMemberOfOrganizationException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(e.getMessage());
    }

    @ExceptionHandler(TenderNotFoundException.class)
    public ResponseEntity<?> handleTenderNotFoundException(TenderNotFoundException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(e.getMessage());
    }
}
