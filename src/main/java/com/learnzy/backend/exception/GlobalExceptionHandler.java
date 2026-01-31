package com.learnzy.backend.exception;

import com.learnzy.backend.exception.custom.*;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        return new ResponseEntity<>(ErrorResponse.builder().error("Not Found").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEnrollmentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEnrollmentException(DuplicateEnrollmentException exception){
        return new ResponseEntity<>(ErrorResponse.builder().error("Duplicate Enrollment").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(DuplicateEmailException exception){
        return new ResponseEntity<>(ErrorResponse.builder().error("Duplicate Email").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception){
        return new ResponseEntity<>(ErrorResponse.builder().error("Forbidden").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorisedException(UnauthorisedException exception){
        return new ResponseEntity<>(ErrorResponse.builder().error("Unauthorised").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception){
        StringBuilder errors = new StringBuilder();

        errors.append(
        exception.getBindingResult().getFieldErrors()
                .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList()));

        return new ResponseEntity<>(ErrorResponse.builder().error("Bad Request").message(errors.toString()).timestamp(OffsetDateTime.now()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(ErrorResponse.builder().error("Bad request").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        return new ResponseEntity<>(ErrorResponse.builder().error("Internal Server Error").message(exception.getMessage()).timestamp(OffsetDateTime.now()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
