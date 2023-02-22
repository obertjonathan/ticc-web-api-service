package com.ticc.webapiservice.exception.exts.merchant;

public class MerchantAlreadyExistsException extends RuntimeException {
    public MerchantAlreadyExistsException(String message) {
        super(message);
    }
}
