package com.differz.bc.config;

import com.differz.bc.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.util.NoSuchElementException;

import static org.zalando.problem.Status.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class ApplicationExceptionHandler implements ProblemHandling {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException e, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withTitle(Status.NOT_FOUND.getReasonPhrase())
                .withDetail(e.getMessage() != null ? e.getMessage() : "Requested element not found")
                .build();
        return create(e, problem, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Problem> handleBadRequestException(BadRequestException e, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(Status.BAD_REQUEST.getReasonPhrase())
                .withDetail(e.getMessage() != null ? e.getMessage() : "Invalid request body")
                .build();
        return create(e, problem, request);
    }

    @Override
    public StatusType defaultConstraintViolationStatus() {
        return UNPROCESSABLE_ENTITY;
    }
}