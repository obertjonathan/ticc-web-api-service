package com.ticc.webapiservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ticc.webapiservice.dto.request.TierRequestDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.exception.exts.tier.TierNotFoundException;
import com.ticc.webapiservice.repository.TicketRepository;
import com.ticc.webapiservice.repository.TierRepository;

@SpringBootTest
public class TierServiceTest {
    @Autowired
    private TierService tierService;

    @MockBean
    private TierRepository tierRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @Test
    void testAddTier() {

        TierRequestDTO tierRequestDTO = new TierRequestDTO();
        tierRequestDTO.setTicketId(1L);
        tierRequestDTO.setName("VIP");
        tierRequestDTO.setDescription("VIP tier");
        tierRequestDTO.setPrice(100L);
        tierRequestDTO.setQty(100);

        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Tier tier = new Tier();
        tier.setTicket(ticket);
        tier.setName("VIP");
        tier.setDescription("VIP tier");
        tier.setPrice(100L);
        tier.setQty(100);

        when(tierRepository.save(tier)).thenReturn(tier);

        // When
        TierResponseDTO result = tierService.addTier(tierRequestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTicketId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("VIP");
        assertThat(result.getDescription()).isEqualTo("VIP tier");
        assertThat(result.getPrice()).isEqualTo(100L);
        assertThat(result.getQty()).isEqualTo(100);
    }

    @Test
    void testDelTier() {
        Long id = 1L;
        when(tierRepository.findById(id)).thenReturn(Optional.of(new Tier()));
        boolean result = tierService.delTier(id);
        assertTrue(result);
        verify(tierRepository, times(1)).save(any(Tier.class));
    }

    @Test
    void testDelTierInvalid(){
        // Arrange
        Long id = 1L;

        // Act and Assert
        assertThrows(TierNotFoundException.class, () -> tierService.delTier(id));
    }

    @Test
    void testUpdateTier() {
        Long id = 1L;
        TierRequestDTO tierRequestDTO = new TierRequestDTO();
        tierRequestDTO.setTicketId(2L);
        tierRequestDTO.setName("test");
        tierRequestDTO.setDescription("test");
        tierRequestDTO.setPrice(100L);
        tierRequestDTO.setQty(10);

        Ticket ticket = new Ticket();
        when(ticketRepository.findById(tierRequestDTO.getTicketId())).thenReturn(Optional.of(ticket));
        Tier tier = new Tier();
        when(tierRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(tier));
        when(tierRepository.save(tier)).thenReturn(tier);

        // when
        TierResponseDTO tierResponseDTO = tierService.updateTier(tierRequestDTO, id);

        // then
        assertEquals(tierRequestDTO.getName(), tierResponseDTO.getName());
        assertEquals(tierRequestDTO.getDescription(), tierResponseDTO.getDescription());
        assertEquals(tierRequestDTO.getPrice(), tierResponseDTO.getPrice());
        assertEquals(tierRequestDTO.getQty(), tierResponseDTO.getQty());

    }

    @Test
    void testFindById() {
         // create a sample tier
         Tier tier = new Tier();
         tier.setId(1L);
         tier.setName("Bronze");
 
         // mock the repository method
         when(tierRepository.findById(anyLong())).thenReturn(Optional.of(tier));
 
         // call the service method
         Optional<Tier> result = tierService.findById(1L);
 
         // assert the result
         assertTrue(result.isPresent());
         assertEquals("Bronze", result.get().getName());
    }

}
