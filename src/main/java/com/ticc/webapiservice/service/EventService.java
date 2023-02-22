package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.request.EventRequestDTO;
import com.ticc.webapiservice.dto.response.EventDetailResponseDTO;
import com.ticc.webapiservice.dto.response.EventResponseDTO;

public interface EventService {

    EventResponseDTO addEvent(EventRequestDTO eventRequestDTO);

    EventDetailResponseDTO findById(Long id);

    EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO);

    boolean delEvent(Long id);
}
