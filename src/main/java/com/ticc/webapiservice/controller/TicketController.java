package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.request.TicketRequestDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.dto.response.TicketResponseDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.exception.exts.ticket.TicketNotFoundException;
import com.ticc.webapiservice.service.EventService;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.TicketService;
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
@Tag(name = "Ticket", description = "Ticket API, This API is used to manage tickets.")
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final EventService eventService;
    private final MerchantAuthService merchantAuthService;
    private static String error = "Unauthorized merchant!";

    @PostMapping
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<BaseResponse<TicketResponseDTO>> addTicket(@RequestBody TicketRequestDTO ticketRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(ticketRequestDTO.getEventId()).getMerchantUsername())) {
            TicketResponseDTO ticket = ticketService.addTicket(ticketRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Ticket created successfully", ticket));
        }
        throw new MerchantRequestNotValidException(error);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket by specific ID")
    public ResponseEntity<BaseResponse<TicketResponseDTO>> updateTicket(@PathVariable("id") Long id, @RequestBody TicketRequestDTO ticketRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(ticketRequestDTO.getEventId()).getMerchantUsername())) {
            TicketResponseDTO ticket = ticketService.updateTicket(id, ticketRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Ticket updated successfully", ticket));
        }
        throw new MerchantRequestNotValidException(error);
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Delete an existing ticket by specific ID")
    public ResponseEntity<BaseResponse<Object>> delTicket(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        Ticket ticket = ticketService.findById(id).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (merchant.getUsername().equals(ticket.getEvent().getMerchant().getUsername())) {
            ticketService.delTicket(id);
            return ResponseEntity.ok(BaseResponse.of("00", "Ticket deleted successfully", ""));
        }
        throw new MerchantRequestNotValidException(error);

    }

    @GetMapping("/{id}/tiers")
    @Operation(summary = "Get ticket tiers")
    public ResponseEntity<BaseResponse<List<TierResponseDTO>>> getTicketTiers(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        Ticket ticket = ticketService.findById(id).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (merchant.getUsername().equals(ticket.getEvent().getMerchant().getUsername())) {
            List<TierResponseDTO> tierResponseDTO = ticketService.findTicketTier(id);
            return ResponseEntity.ok(BaseResponse.of("00", "Ticket tiers retrieved successfully", tierResponseDTO));
        }
        throw new MerchantRequestNotValidException(error);
    }

}
