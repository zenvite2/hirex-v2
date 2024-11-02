package com.ptit.hirex.exception;

import com.ptit.hirex.model.ResponseDto;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ResponseDto<?>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getCode()).body(ResponseDto.builder().success(false).message(e.getMessage()).build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleApiException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(400).body(ResponseDto.builder().success(false).message(e.getFieldError().getDefaultMessage()).build());
    }

    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<ResponseDto<?>> handleApiException(MessagingException e) {
        return ResponseEntity.status(500).body(ResponseDto.builder().success(false).message(e.getMessage()).build());
    }
}
