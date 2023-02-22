package com.ticc.webapiservice.exception.exts.paymentchannel;

public class PaymentChannelNotFoundException extends RuntimeException {
    public PaymentChannelNotFoundException(String message) {
        super(message);
    }
}
