package com.ticc.webapiservice.service;

import java.util.List;

import com.ticc.webapiservice.dto.request.EventImageRequestDTO;
import com.ticc.webapiservice.dto.response.EventImageResponseDTO;

public interface EventImageService {
    EventImageResponseDTO addEventImage(EventImageRequestDTO eventImageRequestDTO);
    List<EventImageResponseDTO> getEventImageByEventId(Long id);
    EventImageResponseDTO updateEventImage(Long id, EventImageRequestDTO eventImageRequestDTO);
    boolean delEventImage(Long id);
    EventImageResponseDTO findById(Long id);
}
