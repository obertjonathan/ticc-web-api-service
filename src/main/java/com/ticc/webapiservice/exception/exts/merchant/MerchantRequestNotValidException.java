package com.ticc.webapiservice.exception.exts.merchant;

public class MerchantRequestNotValidException extends RuntimeException {
    public MerchantRequestNotValidException(String message) {
        super(message);
    }
}
