package com.ticc.webapiservice.exception;

import com.ticc.webapiservice.exception.exts.tier.TierNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticc.webapiservice.dto.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class TierErrorHandler {
    private static String error = "Error: ";

    @ExceptionHandler(value = TierNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(TierNotFoundException e) {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("12", "Tier not found: " + e.getMessage()),HttpStatus.NOT_FOUND);
    }

}
