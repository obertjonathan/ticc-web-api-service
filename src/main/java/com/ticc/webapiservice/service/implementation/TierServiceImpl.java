package com.ticc.webapiservice.service.implementation;

import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.exception.exts.ticket.TicketNotFoundException;
import com.ticc.webapiservice.exception.exts.tier.TierNotFoundException;
import com.ticc.webapiservice.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ticc.webapiservice.dto.request.TierRequestDTO;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.repository.TierRepository;
import com.ticc.webapiservice.service.TierService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TierServiceImpl implements TierService{
    private final TierRepository tierRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private static String notFound = "Tier not found";

    @Override
    public TierResponseDTO addTier(TierRequestDTO tierRequestDTO) {
        Tier tier = new Tier();

        Ticket ticket = ticketRepository.findById(tierRequestDTO.getTicketId()).orElseThrow(() -> new TicketNotFoundException(notFound));
        ticket.setId(tierRequestDTO.getTicketId());

        tier.setTicket(ticket);
        tier.setName(tierRequestDTO.getName());
        tier.setDescription(tierRequestDTO.getDescription());
        tier.setPrice(tierRequestDTO.getPrice());
        tier.setQty(tierRequestDTO.getQty());
        log.info("Tier " + tier.getName() + " created");
        return convertToDto(tierRepository.save(tier));
    }

    @Override
    public TierResponseDTO updateTier(TierRequestDTO tierRequestDTO, Long id) {
        Tier tier = tierRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new TierNotFoundException(notFound));

        Ticket ticket = ticketRepository.findById(tierRequestDTO.getTicketId()).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        ticket.setId(tierRequestDTO.getTicketId());

        tier.setTicket(ticket);
        tier.setName(tierRequestDTO.getName());
        tier.setDescription(tierRequestDTO.getDescription());
        tier.setPrice(tierRequestDTO.getPrice());
        tier.setQty(tierRequestDTO.getQty());
        log.info("Tier " + tier.getName() + " updated");
        return convertToDto(tierRepository.save(tier));
    }


    @Override
    public boolean delTier(Long id) {
        Optional<Tier> tiers = tierRepository.findById(id);

        if (tiers.isEmpty()){
            throw new TierNotFoundException("Tier not found");
        }

        Tier tier = tiers.get();
        tier.setDeletedAt(LocalDateTime.now());
        tierRepository.save(tier);
        return true;
    }

    @Override
    public Optional<Tier> findById(Long id) {
        return tierRepository.findById(id);
    }

    private TierResponseDTO convertToDto(Tier tier) {
        return modelMapper.map(tier, TierResponseDTO.class);
    }

}
