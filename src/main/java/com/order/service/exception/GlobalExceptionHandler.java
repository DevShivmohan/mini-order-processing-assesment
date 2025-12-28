package com.order.service.exception;

import com.order.service.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleGenericException(GenericException genericException) {
        log.error(genericException);
        return ResponseEntity.status(genericException.getStatusCode())
                .body(new ErrorResponse(genericException.getErrorMessage(), genericException.getStatusCode()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error(ex);
        Map<String, String> map = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors().forEach(error -> map.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
