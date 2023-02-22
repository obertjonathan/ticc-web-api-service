package com.ticc.webapiservice.service;

import java.util.List;

import com.ticc.webapiservice.dto.request.MerchantAddRequestDTO;
import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;

public interface MerchantService {
    MerchantResponseDTO addMerchant(MerchantAddRequestDTO merchantAddRequestDTO);
    MerchantResponseDTO getMerchantByUsername(String username);
    List<EventResponseDTO> getEventByMerchant(String username);
}
