package com.ticc.webapiservice.exception;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.exception.exts.event.EventNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class EventErrorHandler {
    private static String error = "Error: ";
    @ExceptionHandler(value = EventNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(EventNotFoundException e) {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("12", "Event not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
