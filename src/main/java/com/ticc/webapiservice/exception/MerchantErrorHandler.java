package com.ticc.webapiservice.exception;

import com.ticc.webapiservice.exception.exts.merchant.MerchantAlreadyExistsException;
import com.ticc.webapiservice.exception.exts.merchant.MerchantNotFoundException;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticc.webapiservice.dto.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class MerchantErrorHandler {

    private static String error ="Error: ";
 
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.error(error,e);
        return ResponseEntity.internalServerError().body(BaseResponse.of("11","Internal Server Error: " + e.getMessage()));
    }

    @ExceptionHandler(value = MerchantNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(MerchantNotFoundException e) {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("12", "Merchant not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MerchantRequestNotValidException.class)
    public ResponseEntity<Object> handleRequestNotValidException(MerchantRequestNotValidException e)
    {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("14", "Request Not Valid: "+e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MerchantAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistException(MerchantAlreadyExistsException e)
    {
        log.error(error,e);
        return new ResponseEntity<>(BaseResponse.of("13", "Merchant Already Exist: " + e.getMessage()), HttpStatus.CONFLICT);
    }
    
}
