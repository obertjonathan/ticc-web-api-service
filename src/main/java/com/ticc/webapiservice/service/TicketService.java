package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.request.TicketRequestDTO;
import com.ticc.webapiservice.dto.response.TicketResponseDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketResponseDTO addTicket(TicketRequestDTO ticketRequestDTO);

    TicketResponseDTO updateTicket(Long id, TicketRequestDTO ticketRequestDTO);

    boolean delTicket(Long id);

    List<TierResponseDTO> findTicketTier(Long id);

    Optional<Ticket> findById(Long id);
}
