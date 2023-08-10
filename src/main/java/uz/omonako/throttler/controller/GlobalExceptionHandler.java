package uz.omonako.throttler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.omonako.throttler.exceptions.LimitExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity handleGlobalException() {
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
}
