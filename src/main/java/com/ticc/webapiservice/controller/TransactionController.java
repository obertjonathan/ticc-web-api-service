package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.dto.response.TransactionResponseDTO;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.TransactionService;
import com.ticc.webapiservice.util.TokenExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction API, This API is used to manage transactions.")
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final MerchantAuthService merchantAuthService;

    @GetMapping("/{username}")
    @Operation(summary = "Get transaction tickets of a specific transaction")
    public ResponseEntity<BaseResponse<List<TransactionResponseDTO>>> getTransactionTickets(@PathVariable("username") String username, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(username)) {
            List<TransactionResponseDTO> transactionDetails = transactionService.getTransactionDetails(username);
            return ResponseEntity.ok(BaseResponse.of("00", "Transactions retrieved successfully", transactionDetails));
        }
        throw new MerchantRequestNotValidException("Unauthorized merchant!");

    }
}
