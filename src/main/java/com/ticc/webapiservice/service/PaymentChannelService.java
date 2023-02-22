package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.response.PaymentChannelResponseDTO;

import java.util.List;

public interface PaymentChannelService {
    List<PaymentChannelResponseDTO> getPaymentChannels();

    PaymentChannelResponseDTO updateStatus(Long id);
}
