package com.sportzone21.server.exception;

import com.sportzone21.server.model.ValidationError;
import com.sportzone21.server.model.ValidationErrorResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String VALIDATION_FAILED_GENERIC_MESSAGE_CODE = "validation.failed.generic";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    private final Environment environment;

    private GlobalExceptionHandler(final Environment environment) {
        this.environment = environment;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        int responseStatus = HttpStatus.BAD_REQUEST.value();
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> {
                    String objectName = e.getObjectName();
                    String fieldName = e.getField();
                    Object rejectedValue = e.getRejectedValue();
                    String defaultMessage = e.getDefaultMessage();
                    String messageCode = VALIDATION_FAILED_GENERIC_MESSAGE_CODE;
                    if (defaultMessage != null && defaultMessage.startsWith("[") && defaultMessage.endsWith("]")) {
                        messageCode = defaultMessage.substring(1, defaultMessage.length() - 1);
                    }
                    String message = environment.getProperty(messageCode);
                    if (message == null) {
                        messageCode = Objects.requireNonNull(e.getCode()).toLowerCase();
                        message = environment.getProperty(messageCode);
                        if (message == null) {
                            messageCode = VALIDATION_FAILED_GENERIC_MESSAGE_CODE;
                            message = environment.getProperty(VALIDATION_FAILED_GENERIC_MESSAGE_CODE);
                        }
                    }
                    return new ValidationError(objectName, fieldName, rejectedValue, messageCode, message);
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(responseStatus)
                .body(new ValidationErrorResponse(responseStatus, VALIDATION_FAILED_MESSAGE, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleServerException(Exception ex) {
        ApiException apiException = new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE, ex.getMessage(), ex);
        return handleApiException(apiException);
    }

    @ExceptionHandler(ProblemException.class)
    public ResponseEntity<Problem> handleApiException(ProblemException ex) {
        return buildResponse(ex.getProblem());
    }

    private static ResponseEntity<Problem> buildResponse(Problem problem) {
        return ResponseEntity.status(problem.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(problem);
    }

}