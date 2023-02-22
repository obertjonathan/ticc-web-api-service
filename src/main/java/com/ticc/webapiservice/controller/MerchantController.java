package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.MerchantService;
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
@RequestMapping("/api/v1/merchants")
@Tag(name = "Merchants", description = "Merchant API, This API is used to get merchant's events.")
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;
    private final MerchantAuthService merchantAuthService;

    @GetMapping("/{username}/events")
    @Operation(summary = "Get events by merchant username")
    public ResponseEntity<BaseResponse<List<EventResponseDTO>>> getMerchantEvent(@PathVariable String username, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(username)) {
            List<EventResponseDTO> events = merchantService.getEventByMerchant(username);
            return ResponseEntity.ok(BaseResponse.of("00", "Successfully retrieved events",events));
        }
        throw new MerchantRequestNotValidException("Unauthorized merchant!");
    }

}
