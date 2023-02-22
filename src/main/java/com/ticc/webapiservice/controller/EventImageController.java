package com.ticc.webapiservice.controller;

import java.util.List;

import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.service.EventService;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.util.TokenExtractor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.request.EventImageRequestDTO;
import com.ticc.webapiservice.dto.response.EventImageResponseDTO;
import com.ticc.webapiservice.service.EventImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Tag(name = "Event Images", description = "Event images API, This API is used to manage event images.")
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/api/v1/event-images")
@Slf4j
public class EventImageController {
    private final EventImageService eventImageService;
    private final MerchantAuthService merchantAuthService;
    private final EventService eventService;
    private static String error = "Unauthorized merchant!";

    @PostMapping
    public ResponseEntity<BaseResponse<EventImageResponseDTO>> addEventImage(@RequestBody EventImageRequestDTO eventImageRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(eventImageRequestDTO.getEventId()).getMerchantUsername())) {
            EventImageResponseDTO eventImage = eventImageService.addEventImage(eventImageRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Event image saved successfully", eventImage));
        }
        throw new MerchantRequestNotValidException(error);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<List<EventImageResponseDTO>>> getEventImage(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(id).getMerchantUsername())) {
            List<EventImageResponseDTO> eventImages = eventImageService.getEventImageByEventId(id);
            return ResponseEntity.ok(BaseResponse.of("00", "Event image retrieved successfully", eventImages));
        }
        throw new MerchantRequestNotValidException(error);

    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<EventImageResponseDTO>> updateEventImage(@PathVariable Long id, @RequestBody EventImageRequestDTO eventImageRequestDTO, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(eventImageService.findById(id).getEventId()).getMerchantUsername())) {
            EventImageResponseDTO responseDTO = eventImageService.updateEventImage(id, eventImageRequestDTO);
            return ResponseEntity.ok(BaseResponse.of("00", "Event image updated successfully", responseDTO));
        }
        throw new MerchantRequestNotValidException(error);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> delEventImage(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        String token = TokenExtractor.extractToken(headers);
        MerchantResponseDTO merchant = merchantAuthService.decodeToken(token);

        if (merchant.getUsername().equals(eventService.findById(eventImageService.findById(id).getEventId()).getMerchantUsername())) {
            eventImageService.delEventImage(id);
            return ResponseEntity.ok(BaseResponse.of("00", "Event deleted successfully", ""));
        }
        throw new MerchantRequestNotValidException(error);
    }
}
