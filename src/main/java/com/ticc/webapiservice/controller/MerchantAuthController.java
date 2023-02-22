package com.ticc.webapiservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.request.MerchantAddRequestDTO;
import com.ticc.webapiservice.dto.request.MerchantRequestDTO;
import com.ticc.webapiservice.dto.response.MerchantLoginResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.MerchantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/merchants")
@Tag(name = "Merchants auth", description = "Auth API, This API is used to manage merchant authentication.")
@RequiredArgsConstructor
@Slf4j
public class MerchantAuthController {
    
    private final MerchantAuthService merchantAuthService;
    private final MerchantService merchantService;

    @PostMapping("/auth/login")
    @Operation(summary = "Login Merchant User")
    public ResponseEntity<BaseResponse<MerchantLoginResponseDTO>> login(@RequestBody MerchantRequestDTO merchantRequestDTO)
    {
        log.debug("login: {}", merchantRequestDTO);
        String token= merchantAuthService.validateAndGenerated(merchantRequestDTO.getUsername(), merchantRequestDTO.getPassword());
        if(token==null)
        {
            return ResponseEntity.badRequest().body(BaseResponse.of("99","Invalid username or password"));
        }
        MerchantResponseDTO merchantResponseDTO = merchantService.getMerchantByUsername(merchantRequestDTO.getUsername());
        MerchantLoginResponseDTO merchantLoginResponseDTO = MerchantLoginResponseDTO.of(token, merchantResponseDTO.getUsername(), merchantResponseDTO.getName());
        return ResponseEntity.ok(BaseResponse.of("00","Success",merchantLoginResponseDTO));
    }

    @PostMapping("/auth/register")
    @Operation(summary = "Register Merchant User")
    public ResponseEntity<BaseResponse<MerchantResponseDTO>> register(@RequestBody MerchantAddRequestDTO merchantAddRequestDTO)
    {
        log.debug("register: {}",merchantAddRequestDTO);
        MerchantResponseDTO merchantResponseDTO = merchantService.addMerchant(merchantAddRequestDTO);
        return ResponseEntity.ok(BaseResponse.of("00", "Merchant user with username "+merchantResponseDTO.getUsername()+" registered",merchantResponseDTO));
    }


}
