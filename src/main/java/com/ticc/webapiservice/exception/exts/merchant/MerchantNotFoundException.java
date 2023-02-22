package com.ticc.webapiservice.exception.exts.merchant;

public class MerchantNotFoundException extends RuntimeException {
    public MerchantNotFoundException(String message) {
        super(message);
    }
}
