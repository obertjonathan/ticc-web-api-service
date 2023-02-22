package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.response.PaymentChannelResponseDTO;
import com.ticc.webapiservice.service.PaymentChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentChannelControllerTest {
    @Mock
    PaymentChannelService paymentChannelService;

    PaymentChannelController paymentChannelController;

    @BeforeEach
    void setup() {
        paymentChannelController = new PaymentChannelController(paymentChannelService);
    }

    @Test
    void getPaymentChannels() {
        PaymentChannelResponseDTO paymentChannel = new PaymentChannelResponseDTO(1L,"Virtual Account", "BCA VA", true);
        List<PaymentChannelResponseDTO> responseDTO = List.of(paymentChannel);

        given(paymentChannelService.getPaymentChannels()).willReturn(responseDTO);

        ResponseEntity<BaseResponse<List<PaymentChannelResponseDTO>>> response = paymentChannelController.getPaymentChannels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateStatus() {
        PaymentChannelResponseDTO paymentChannel = new PaymentChannelResponseDTO(1L,"Virtual Account", "BCA VA", true);

        given(paymentChannelService.updateStatus(anyLong())).willReturn(paymentChannel);

        ResponseEntity<BaseResponse<PaymentChannelResponseDTO>> response = paymentChannelController.updateStatus(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentChannel, Objects.requireNonNull(response.getBody()).getData());
    }
}