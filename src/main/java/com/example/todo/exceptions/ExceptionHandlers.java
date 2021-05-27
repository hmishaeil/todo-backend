package com.example.todo.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlers {

    @Autowired
    ExceptionResponse response;

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        response.setStatus(HttpStatus.BAD_REQUEST.name());
        response.setErrors(errors);
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> resourceAlreadyExists(ResourceAlreadyExistsException ex) {

        response.setStatus(HttpStatus.CONFLICT.name());
        response.setErrors(Arrays.asList(ex.getMessage()));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException ex) {

        response.setStatus(HttpStatus.NOT_FOUND.name());
        response.setErrors(Arrays.asList(ex.getMessage()));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ EmailNotVerifiedException.class, SendingEmailException.class,
            HttpMessageNotReadableException.class })
    public ResponseEntity<ExceptionResponse> emailNotVerifiedYet(RuntimeException ex) {

        response.setStatus(HttpStatus.BAD_REQUEST.name());
        response.setErrors(Arrays.asList(ex.getMessage()));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler(SendingEmailException.class)
    // public ResponseEntity<ExceptionResponse>
    // sendingEmailFailed(SendingEmailException ex) {

    // response.setStatus(HttpStatus.BAD_REQUEST.name());
    // response.setErrors(Arrays.asList(ex.getMessage()));
    // response.setTimestamp(LocalDateTime.now());

    // return new ResponseEntity<ExceptionResponse>(response,
    // HttpStatus.BAD_REQUEST);
    // }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {

        response.setStatus(HttpStatus.UNAUTHORIZED.name());
        response.setErrors(Arrays.asList(ex.getMessage()));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler({ InternalServerException.class })
    public ResponseEntity<ExceptionResponse> handleInternalServerException(InternalServerException ex) {

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
        response.setErrors(Arrays.asList(ex.getMessage()));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler({ DisabledException.class })
    public ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException ex) {

        response.setStatus(HttpStatus.BAD_REQUEST.name());
        response.setErrors(Arrays.asList("User has been disabled."));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ExceptionResponse> handleBadCredentialException(BadCredentialsException ex) {

        response.setStatus(HttpStatus.BAD_REQUEST.name());
        response.setErrors(Arrays.asList("Invalid credential is provided."));
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);

    }

}
