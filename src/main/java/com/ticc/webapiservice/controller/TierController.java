package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.exception.exts.ticket.TicketNotFoundException;
import com.ticc.webapiservice.exception.exts.tier.TierNotFoundException;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.TicketService;
import com.ticc.webapiservice.util.TokenExtractor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.request.TierRequestDTO;
import com.ticc.webapiservice.service.TierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tier", description = "Tier API, This API is used to manage ticket tiers.")
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/api/v1/tiers")
public class TierController {

    private final TierService tierService;
    private final TicketService ticketService;
    private final MerchantAuthService merchantAuthService;
    private static String error = "Unauthorized merchant";
    
    @PostMapping
    @Operation(summary = "Create new Tier")
    public ResponseEntity<BaseResponse<TierResponseDTO>> createTier(@RequestBody TierRequestDTO tierRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        Ticket ticket = ticketService.findById(tierRequestDTO.getTicketId()).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (merchant.getUsername().equals(ticket.getEvent().getMerchant().getUsername())) {
            TierResponseDTO tiers = tierService.addTier(tierRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Tier created successfully", tiers));
        }
        throw new MerchantRequestNotValidException(error);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Tier Data by Specific Id")
    public ResponseEntity<BaseResponse<TierResponseDTO>> updateTier(@PathVariable Long id,@RequestBody TierRequestDTO tierRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        Ticket ticket = ticketService.findById(tierRequestDTO.getTicketId()).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (merchant.getUsername().equals(ticket.getEvent().getMerchant().getUsername())) {
            TierResponseDTO tiers = tierService.updateTier(tierRequestDTO, id);
            return ResponseEntity.ok(BaseResponse.of("00", "Tier updated successfully", tiers));
        }
        throw new MerchantRequestNotValidException(error);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Delete Tier Data by Specific Id")
    public ResponseEntity<BaseResponse<Object>> deleteTier(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        Tier tier = tierService.findById(id).orElseThrow(() -> new TierNotFoundException("Tier not found"));

        if (merchant.getUsername().equals(tier.getTicket().getEvent().getMerchant().getUsername())) {
            tierService.delTier(id);
            return ResponseEntity.ok(BaseResponse.of("00", "Tier deleted successfully", ""));
        }
        throw new MerchantRequestNotValidException(error);
    }
}
