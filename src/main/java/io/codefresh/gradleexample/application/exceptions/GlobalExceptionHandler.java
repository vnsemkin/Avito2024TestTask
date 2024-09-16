package io.codefresh.gradleexample.application.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String REASON = "reason: ";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(REASON + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(REASON + e.getMessage());
    }

    @ExceptionHandler(UserNotMemberOfOrganizationException.class)
    public ResponseEntity<?> handleUserNotMemberOfOrganization(UserNotMemberOfOrganizationException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(REASON + e.getMessage());
    }

    @ExceptionHandler(TenderNotFoundException.class)
    public ResponseEntity<?> handleTenderNotFoundException(TenderNotFoundException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(REASON + e.getMessage());
    }

    @ExceptionHandler(AuthorIdNotMatchWithAuthorTypeException.class)
    public ResponseEntity<?> handleAuthorIdNotMatchWithAuthorTypeException(AuthorIdNotMatchWithAuthorTypeException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(REASON + e.getMessage());
    }
    @ExceptionHandler(BidNotFoundException.class)
    public ResponseEntity<?> handleBidNotFoundException(BidNotFoundException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(REASON + e.getMessage());
    }

    @ExceptionHandler(UserAlreadySubmittedDecisionException.class)
    public ResponseEntity<?> handleUserAlreadySubmittedDecisionException(UserAlreadySubmittedDecisionException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(REASON + e.getMessage());
    }

    @ExceptionHandler(UserAlreadyLeftFeedbackException.class)
    public ResponseEntity<?> handleUserAlreadyLeftFeedbackException(UserAlreadyLeftFeedbackException e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(REASON + e.getMessage());
    }
}
