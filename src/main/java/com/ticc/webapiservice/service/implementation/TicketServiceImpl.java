package com.ticc.webapiservice.service.implementation;

import com.ticc.webapiservice.dto.request.TicketRequestDTO;
import com.ticc.webapiservice.dto.response.TicketResponseDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.exception.exts.event.EventNotFoundException;
import com.ticc.webapiservice.exception.exts.ticket.TicketNotFoundException;
import com.ticc.webapiservice.repository.EventRepository;
import com.ticc.webapiservice.repository.TicketRepository;
import com.ticc.webapiservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private static String ticketNotFound = "Ticket not found";

    @Override
    public TicketResponseDTO addTicket(TicketRequestDTO ticketRequestDTO) {
        Ticket ticket = new Ticket();

        Event event = eventRepository.findById(ticketRequestDTO.getEventId()).orElseThrow(() -> new EventNotFoundException("Event not found"));
        event.setId(ticketRequestDTO.getEventId());

        ticket.setEvent(event);
        ticket.setName(ticketRequestDTO.getName());
        ticket.setDate(ticketRequestDTO.getDate());
        log.info("Ticket " + ticket.getName() + " created");
        return convertToDto(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO ticketRequestDTO) {
        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new TicketNotFoundException(ticketNotFound));

        Event event = eventRepository.findByIdAndDeletedAtIsNull(ticketRequestDTO.getEventId()).orElseThrow(() -> new EventNotFoundException("Event not found"));
        event.setId(ticketRequestDTO.getEventId());

        ticket.setEvent(event);
        ticket.setName(ticketRequestDTO.getName());
        ticket.setDate(ticketRequestDTO.getDate());
        log.info("Ticket " + ticket.getName() + " updated");
        return convertToDto(ticketRepository.save(ticket));
    }


    @Override
    public boolean delTicket(Long id) {
        Optional<Ticket> tickets = ticketRepository.findById(id);

        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found");
        }

        Ticket ticket = tickets.get();
        ticket.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticket);

        return true;
    }

    @Override
    public List<TierResponseDTO> findTicketTier(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(ticketNotFound));
        return ticket.getTiers().stream().filter(e -> e.getDeletedAt() == null).map(this::convertToDto).toList();
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    private TierResponseDTO convertToDto(Tier tier) {
        return modelMapper.map(tier, TierResponseDTO.class);
    }

    private TicketResponseDTO convertToDto(Ticket ticket) {
        return modelMapper.map(ticket, TicketResponseDTO.class);
    }

}
