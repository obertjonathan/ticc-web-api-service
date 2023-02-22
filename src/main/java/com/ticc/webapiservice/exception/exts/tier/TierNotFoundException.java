package com.ticc.webapiservice.exception.exts.tier;

public class TierNotFoundException extends RuntimeException {
    public TierNotFoundException(String message) {
        super(message);
    }
}
