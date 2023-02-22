package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.request.TierRequestDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Tier;

import java.util.Optional;

public interface TierService {
    TierResponseDTO addTier(TierRequestDTO tierRequestDTO);
    TierResponseDTO updateTier(TierRequestDTO tierRequestDTO, Long id);
    boolean delTier(Long id);
    Optional<Tier> findById(Long id);
}
