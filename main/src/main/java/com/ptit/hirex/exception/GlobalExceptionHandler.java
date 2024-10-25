package com.ptit.hirex.exception;

import com.ptit.hirex.model.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getCode()).body(ApiResponse.builder().code(e.getCode()).message(e.getMessage()).build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(400).body(ApiResponse.builder().code(400).message(e.getFieldError().getDefaultMessage()).build());
    }

    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(MessagingException e) {
        return ResponseEntity.status(500).body(ApiResponse.builder().code(500).message(e.getMessage()).build());
    }
}
