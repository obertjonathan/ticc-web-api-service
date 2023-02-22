package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.response.MerchantResponseDTO;

public interface MerchantAuthService {
    String validateAndGenerated(String username,String password);

    MerchantResponseDTO decodeToken(String token);
}
