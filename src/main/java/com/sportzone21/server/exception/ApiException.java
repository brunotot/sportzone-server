package com.sportzone21.server.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends ProblemException {

	private static final long serialVersionUID = 1L;

	public ApiException(HttpStatus status, String title, String details, Throwable cause) {
        super(new Problem(status.value(), title, details), cause);
    }

    public ApiException(HttpStatus status, String title, String details) {
        super(new Problem(status.value(), title, details), null);
    }

}