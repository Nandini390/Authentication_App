package org.example.authapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(),404,"User already exists",System.currentTimeMillis());
        return ResponseEntity.status(409).body(error); // 409 Conflict
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(),404,"some error occured",System.currentTimeMillis());
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),404,"Resource not found",System.currentTimeMillis());
        return ResponseEntity.status(404).body(error);
    }
}