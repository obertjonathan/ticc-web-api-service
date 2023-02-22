package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.request.EventRequestDTO;
import com.ticc.webapiservice.dto.response.EventDetailResponseDTO;
import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.entity.*;
import com.ticc.webapiservice.exception.exts.event.EventNotFoundException;
import com.ticc.webapiservice.repository.EventRepository;
import com.ticc.webapiservice.repository.MerchantRepository;
import com.ticc.webapiservice.service.implementation.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    EventRepository eventRepository;
    @Mock
    MerchantRepository merchantRepository;

    @InjectMocks
    EventServiceImpl eventService;
    ModelMapper modelMapper = Mockito.spy(ModelMapper.class);

    @Mock
    Event event;
    @Mock
    Merchant merchant;
    @Mock
    Category category;
    @Mock
    Ticket ticket;
    @Mock
    Tier tier;

    EventRequestDTO eventRequestDTO;
    EventResponseDTO eventResponseDTO;
    EventDetailResponseDTO detailResponseDTO;

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository, merchantRepository, modelMapper);

        merchant.setId("merchant1");
        merchant.setUsername("username");

        category.setId(1L);
        category.setName("Music");

        eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setMerchantUsername("username");
        eventRequestDTO.setCategoryId(1L);
        eventRequestDTO.setName("event name");
        eventRequestDTO.setDate(LocalDate.now());
        eventRequestDTO.setSellEndDate(LocalDate.now());
        eventRequestDTO.setSellEndDate(LocalDate.now());
        eventRequestDTO.setDescription("event desc");
        eventRequestDTO.setLocation("event loc");

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setMerchantUsername("username");
        eventResponseDTO.setCategoryId(1L);
        eventResponseDTO.setName("event name");
        eventResponseDTO.setDate(LocalDate.now());
        eventResponseDTO.setSellEndDate(LocalDate.now());
        eventResponseDTO.setSellEndDate(LocalDate.now());
        eventResponseDTO.setDescription("event desc");
        eventResponseDTO.setLocation("event loc");

        detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setId(1L);
        detailResponseDTO.setMerchantUsername("username");
        detailResponseDTO.setCategoryId(1L);
        detailResponseDTO.setName("event name");
        detailResponseDTO.setDate(LocalDate.now());
        detailResponseDTO.setSellEndDate(LocalDate.now());
        detailResponseDTO.setSellEndDate(LocalDate.now());
        detailResponseDTO.setDescription("event desc");
        detailResponseDTO.setLocation("event loc");
    }

    @Test
    void testAddEvent() {
        when(merchant.getUsername()).thenReturn("username");
        when(merchantRepository.findByUsername(any())).thenReturn(Optional.of(merchant));
        when(event.getMerchant()).thenReturn(merchant);

        when(category.getId()).thenReturn(1L);
        when(event.getCategory()).thenReturn(category);

        when(event.getName()).thenReturn("event name");
        when(event.getDate()).thenReturn(LocalDate.now());
        when(event.getSellStartDate()).thenReturn(LocalDate.now());
        when(event.getSellEndDate()).thenReturn(LocalDate.now());
        when(event.getDescription()).thenReturn("event desc");
        when(event.getLocation()).thenReturn("event loc");
        when(eventRepository.save(any())).thenReturn(event);

        EventResponseDTO responseDTO = eventService.addEvent(eventRequestDTO);

        assertEquals(responseDTO.getName(), eventRequestDTO.getName());

    }

    @Test
    void testDelEvent() {
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        eventService.delEvent(any());
    }

    @Test
    void testFindById() {
        when(merchant.getUsername()).thenReturn("username");
        when(event.getMerchant()).thenReturn(merchant);

        when(category.getId()).thenReturn(1L);
        when(event.getCategory()).thenReturn(category);

        when(event.getName()).thenReturn("event name");
        when(event.getDate()).thenReturn(LocalDate.now());
        when(event.getSellStartDate()).thenReturn(LocalDate.now());
        when(event.getSellEndDate()).thenReturn(LocalDate.now());
        when(event.getDescription()).thenReturn("event desc");
        when(event.getLocation()).thenReturn("event loc");

        when(event.getTickets()).thenReturn(List.of(ticket));
        when(ticket.getId()).thenReturn(1L);

        when(ticket.getTiers()).thenReturn(List.of(tier));
        when(tier.getId()).thenReturn(1L);

        when(eventRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(event));
        EventDetailResponseDTO responseDTO = eventService.findById(any());

        assertEquals(responseDTO.getName(), eventRequestDTO.getName());
    }

    @Test
    void testUpdateEvent() {
        when(eventRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(event));
        when(event.getId()).thenReturn(1L);
        when(merchant.getUsername()).thenReturn("username");
        when(merchantRepository.findByUsername(any())).thenReturn(Optional.of(merchant));
        when(event.getMerchant()).thenReturn(merchant);

        when(category.getId()).thenReturn(1L);
        when(event.getCategory()).thenReturn(category);

        when(event.getName()).thenReturn("event name");
        when(event.getDate()).thenReturn(LocalDate.now());
        when(event.getSellStartDate()).thenReturn(LocalDate.now());
        when(event.getSellEndDate()).thenReturn(LocalDate.now());
        when(event.getDescription()).thenReturn("event desc");
        when(event.getLocation()).thenReturn("event loc");
        when(eventRepository.save(any())).thenReturn(event);

        EventResponseDTO responseDTO = eventService.updateEvent(any(), eventRequestDTO);

        assertEquals(responseDTO.getName(), eventRequestDTO.getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(eventRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.findById(any()));
    }

    @Test
    void testUpdateEventNotFound() {
        when(eventRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(any(), eventRequestDTO));
    }

    @Test
    void testDelEventNotFound() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.delEvent(any()));
    }
}
