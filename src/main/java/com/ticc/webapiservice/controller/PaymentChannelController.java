package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.response.PaymentChannelResponseDTO;
import com.ticc.webapiservice.service.PaymentChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Payment Channel", description = "Payment Channel API, This API is used to manage payment channels.")
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/api/v1/payments")
public class PaymentChannelController {
    private final PaymentChannelService paymentChannelService;

    @GetMapping
    @Operation(summary = "Get all payment channels")
    public ResponseEntity<BaseResponse<List<PaymentChannelResponseDTO>>> getPaymentChannels() {
        List<PaymentChannelResponseDTO> paymentChannels = paymentChannelService.getPaymentChannels();
        return ResponseEntity.ok(BaseResponse.of("00", "Payment channels retrieved successfully", paymentChannels));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a payment channel status by specific ID")
    public ResponseEntity<BaseResponse<PaymentChannelResponseDTO>> updateStatus(@PathVariable("id") Long id) {
        PaymentChannelResponseDTO payment = paymentChannelService.updateStatus(id);
        return ResponseEntity.ok(BaseResponse.of("00", "Payment channel updated successfully", payment));
    }
}
