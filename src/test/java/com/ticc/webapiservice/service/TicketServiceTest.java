package com.ticc.webapiservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ticc.webapiservice.dto.response.TicketResponseDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.*;
import com.ticc.webapiservice.exception.exts.ticket.TicketNotFoundException;
import com.ticc.webapiservice.repository.EventRepository;
import com.ticc.webapiservice.service.implementation.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.ticc.webapiservice.dto.request.TicketRequestDTO;
import com.ticc.webapiservice.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    TicketRepository ticketRepository;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    TicketServiceImpl ticketService;
    ModelMapper modelMapper = Mockito.spy(ModelMapper.class);
    TicketRequestDTO ticketRequestDTO;
    TicketResponseDTO ticketResponseDTO;
    TierResponseDTO tierResponseDTO;
    @Mock
    Event event;
    @Mock
    Category category;
    @Mock
    Merchant merchant;
    @Mock
    Ticket ticket;
    @Mock
    Tier tier;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository, eventRepository, modelMapper);

        merchant.setId("merchant1");
        merchant.setName("merchant");
        merchant.setUsername("username");
        merchant.setEmail("email@mail.com");
        merchant.setBio("bio");
        merchant.setLocation("location");

        category.setName("Music");

        event.setId(1L);
        event.setMerchant(merchant);
        event.setCategory(category);
        event.setName("Event name");

        ticketRequestDTO = new TicketRequestDTO();
        ticketRequestDTO.setEventId(1L);
        ticketRequestDTO.setName("ticket name");
        ticketRequestDTO.setDate(LocalDate.now());

        ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setId(1L);
        ticketResponseDTO.setEventId(1L);
        ticketResponseDTO.setName("ticket name");
        ticketResponseDTO.setDate(LocalDate.now());

        tierResponseDTO = new TierResponseDTO();
        tierResponseDTO.setTicketId(1L);
        tierResponseDTO.setId(1L);
        tierResponseDTO.setName("tier name");
        tierResponseDTO.setDescription("tier desc");
        tierResponseDTO.setPrice(1000L);
        tierResponseDTO.setQty(1);
    }

    @Test
    void testAddTicket() {
        when(event.getId()).thenReturn(1L);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(ticket.getEvent()).thenReturn(event);

        when(ticket.getName()).thenReturn("ticket name");
        when(ticket.getDate()).thenReturn(LocalDate.now());
        when(ticketRepository.save(any())).thenReturn(ticket);

        TicketResponseDTO responseDTO = ticketService.addTicket(ticketRequestDTO);

        assertEquals(responseDTO.getName(), ticketRequestDTO.getName());
    }

    @Test
    void testDelTicket() {
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticket));

        ticketService.delTicket(any());
    }

    @Test
    void testFindTicketTier() {
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticket));
        when(ticket.getTiers()).thenReturn(List.of(tier));

        when(tier.getName()).thenReturn("tier name");

        List<TierResponseDTO> responseDTO = ticketService.findTicketTier(any());

        assertEquals(responseDTO.get(0).getName(), tierResponseDTO.getName());
    }

    @Test
    void testUpdateTicket() {
        when(ticketRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(ticket));

        when(event.getId()).thenReturn(1L);
        when(eventRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(event));
        when(ticket.getEvent()).thenReturn(event);

        when(ticket.getName()).thenReturn("ticket name");
        when(ticket.getDate()).thenReturn(LocalDate.now());
        when(ticketRepository.save(any())).thenReturn(ticket);

        TicketResponseDTO responseDTO = ticketService.updateTicket(any(), ticketRequestDTO);

        assertEquals(responseDTO.getName(), ticketRequestDTO.getName());
    }

    @Test
    void testUpdateTicketNotFound() {
        when(ticketRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket(any(), ticketRequestDTO));
    }

    @Test
    void testDelTicketNotFound() {
        when(ticketRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.delTicket(any()));
    }
}
