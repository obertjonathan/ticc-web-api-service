package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.request.EventRequestDTO;
import com.ticc.webapiservice.dto.response.EventDetailResponseDTO;
import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.service.EventService;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.util.TokenExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Event", description = "Event API, This API is used to manage events.")
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    private final MerchantAuthService merchantAuthService;
    private static String error = "Unauthorized merchant!";

    @PostMapping
    @Operation(summary = "Create a new event")
    public ResponseEntity<BaseResponse<EventResponseDTO>> addEvent(@RequestBody EventRequestDTO eventRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventRequestDTO.getMerchantUsername())) {
            EventResponseDTO event = eventService.addEvent(eventRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Event created successfully", event));
        }
        throw new MerchantRequestNotValidException(error);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an event by specific ID")
    public ResponseEntity<BaseResponse<EventDetailResponseDTO>> findByEventId(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);
        EventDetailResponseDTO event = eventService.findById(id);

        if (merchant.getUsername().equals(event.getMerchantUsername())) {
            return ResponseEntity.ok(BaseResponse.of("00", "Event retrieved successfully", event));
        }
        throw new MerchantRequestNotValidException(error);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an event by specific ID")
    public ResponseEntity<BaseResponse<EventResponseDTO>> updateEvent(@PathVariable("id") Long id, @RequestBody EventRequestDTO eventRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(id).getMerchantUsername())) {
            EventResponseDTO event = eventService.updateEvent(id, eventRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Event updated successfully", event));
        }
        throw new MerchantRequestNotValidException(error);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Delete an existing event by specific ID")
    public ResponseEntity<BaseResponse<Object>> delEvent(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(id).getMerchantUsername())) {
            eventService.delEvent(id);
            return ResponseEntity.ok(BaseResponse.of("00", "Event deleted successfully", ""));
        }
        throw new MerchantRequestNotValidException(error);
    }

}
