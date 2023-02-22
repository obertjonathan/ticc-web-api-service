package com.ticc.webapiservice.service.implementation;

import com.ticc.webapiservice.dto.request.EventRequestDTO;
import com.ticc.webapiservice.dto.response.*;
import com.ticc.webapiservice.entity.*;
import com.ticc.webapiservice.exception.exts.event.EventNotFoundException;
import com.ticc.webapiservice.exception.exts.merchant.MerchantRequestNotValidException;
import com.ticc.webapiservice.repository.EventRepository;
import com.ticc.webapiservice.repository.MerchantRepository;
import com.ticc.webapiservice.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "events")
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final MerchantRepository merchantRepository;
    private final ModelMapper modelMapper;
    private static String notFound = "Event not found";

    @Override
    @CacheEvict(value = "events", allEntries = true)
    public EventResponseDTO addEvent(EventRequestDTO eventRequestDTO) {
        Event event = new Event();

        Merchant merchant = merchantRepository.findByUsername(eventRequestDTO.getMerchantUsername()).orElseThrow(() -> new MerchantRequestNotValidException("Merchant not valid"));
        merchant.setUsername(eventRequestDTO.getMerchantUsername());

        Category category = new Category();
        category.setId(eventRequestDTO.getCategoryId());

        event.setMerchant(merchant);
        event.setCategory(category);
        event.setName(eventRequestDTO.getName());
        event.setDate(eventRequestDTO.getDate());
        event.setSellStartDate(eventRequestDTO.getSellStartDate());
        event.setSellEndDate(eventRequestDTO.getSellEndDate());
        event.setDescription(eventRequestDTO.getDescription());
        event.setLocation(eventRequestDTO.getLocation());
        log.info("Event " + event.getName() + " created");
        return convertToDto(eventRepository.save(event));
    }

    @Override
    public EventDetailResponseDTO findById(Long id) {
        Event event = eventRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new EventNotFoundException(notFound));
        return convertToDetailDto(event);
    }

    @Override
    @CacheEvict(value = "events", allEntries = true)
    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO) {
        Event event = eventRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new EventNotFoundException(notFound));

        Merchant merchant = merchantRepository.findByUsername(eventRequestDTO.getMerchantUsername()).orElseThrow(() -> new MerchantRequestNotValidException("Merchant not valid"));
        merchant.setUsername(eventRequestDTO.getMerchantUsername());

        Category category = new Category();
        category.setId(eventRequestDTO.getCategoryId());

        event.setMerchant(merchant);
        event.setCategory(category);
        event.setName(eventRequestDTO.getName());
        event.setDate(eventRequestDTO.getDate());
        event.setSellStartDate(eventRequestDTO.getSellStartDate());
        event.setSellEndDate(eventRequestDTO.getSellEndDate());
        event.setDescription(eventRequestDTO.getDescription());
        event.setLocation(eventRequestDTO.getLocation());
        log.info("Event " + event.getName() + " updated");
        return convertToDto(eventRepository.save(event));
    }

    @Override
    @CacheEvict(key = "'eventList'", allEntries = true)
    public boolean delEvent(Long id) {
        Optional<Event> events = eventRepository.findById(id);

        if (events.isEmpty()) {
            throw new EventNotFoundException("Event not found");
        }

        Event event = events.get();
        event.setDeletedAt(LocalDateTime.now());
        eventRepository.save(event);

        return true;
    }

    private EventResponseDTO convertToDto(Event event) {
        return modelMapper.map(event, EventResponseDTO.class);
    }

    private TierResponseDTO convertToDto(Tier tier) {
        return modelMapper.map(tier, TierResponseDTO.class);
    }

    private TicketDetailResponseDTO convertToDto(Ticket ticket) {
        TicketDetailResponseDTO ticketDetailResponse = modelMapper.map(ticket, TicketDetailResponseDTO.class);

        // Set relational mapper for tiers
        List<Tier> tiers = ticket.getTiers();
        ticketDetailResponse.setTierResponses(tiers.stream().filter(e -> e.getDeletedAt() == null).map(this::convertToDto).toList());

        return ticketDetailResponse;
    }


    private EventDetailResponseDTO convertToDetailDto(Event event) {
        EventDetailResponseDTO eventDetailResponse = modelMapper.map(event, EventDetailResponseDTO.class);

        // Set relational mapper
        eventDetailResponse.setMerchantUsername(event.getMerchant().getUsername());

        // Set relational mapper for tickets
        List<Ticket> tickets = event.getTickets();
        eventDetailResponse.setTicketResponses(tickets.stream().filter(e -> e.getDeletedAt() == null).map(this::convertToDto).toList());

        return eventDetailResponse;
    }


}
