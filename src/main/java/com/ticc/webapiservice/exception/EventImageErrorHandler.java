package com.ticc.webapiservice.exception;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.exception.exts.eventimage.EventImageAlreadyExistsException;
import com.ticc.webapiservice.exception.exts.eventimage.EventImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class EventImageErrorHandler {
    private static String error = "Error: ";

    @ExceptionHandler(value = EventImageNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(EventImageNotFoundException e) {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("12", "Event image not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EventImageAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExists(EventImageAlreadyExistsException e) {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("13", "Event image already exists: " + e.getMessage()), HttpStatus.CONFLICT);
    }
}
