package com.ticc.webapiservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticc.webapiservice.dto.BaseResponse;
import com.ticc.webapiservice.dto.request.TierRequestDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.dto.response.TierResponseDTO;
import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.TicketService;
import com.ticc.webapiservice.service.TierService;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TierController.class)
public class TierControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TierService tierService;

    @MockBean
    private MerchantAuthService merchantAuthService;

    @MockBean
    private TicketService ticketService;

    private TierResponseDTO tierResponseDTO;

    private TierRequestDTO tierRequestDTO;

    private String jwtToken;

    private MerchantResponseDTO merchantResponseDTO;


    @BeforeEach
    void setUp()
    {
        Tier tier = new Tier();
        tier.setId(1L);
        tier.setName("name");
        tier.setPrice(100L);
        tier.setQty(1);
        tier.setDescription("desc");

        merchantResponseDTO = new MerchantResponseDTO();
        merchantResponseDTO.setId(UUID.randomUUID().toString());
        merchantResponseDTO.setName("name");
        merchantResponseDTO.setUsername("username");
        merchantResponseDTO.setEmail("email@mail.com");
        merchantResponseDTO.setLocation("location");

        tierRequestDTO = new TierRequestDTO();
        tierRequestDTO.setName("name");
        tierRequestDTO.setPrice(1L);
        tierRequestDTO.setQty(1);
        tierRequestDTO.setDescription("desc");
        tierRequestDTO.setTicketId(1L);

        tierResponseDTO = new TierResponseDTO();
        tierResponseDTO.setId(1L);
        tierResponseDTO.setName("name");
        tierResponseDTO.setPrice(1L);
        tierResponseDTO.setDescription("desc");
        tierResponseDTO.setTicketId(1L);
        tierResponseDTO.setQty(1);

        jwtToken = obtainJwtToken();
    }

    private String obtainJwtToken() {
        return "Bearer fake-token";
    }

    @Test
    void testCreateTier() throws Exception {
        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(ticketService.findById(any())).thenReturn(Optional.of(new Ticket() {{
            setEvent(new Event() {{
                setMerchant(new Merchant() {{
                    setUsername("username");
                }});
            }});
        }}));  
        when(tierService.addTier(any())).thenReturn(tierResponseDTO);

        mockMvc.perform(post("/api/v1/tiers")
        .header("Authorization", jwtToken)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(tierRequestDTO))
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("00"))
        .andExpect(jsonPath("$.message").value("Tier created successfully"))
        .andExpect(jsonPath("$.data.id").value(1L))
        .andExpect(jsonPath("$.data.ticket_id").value(1L))
        .andExpect(jsonPath("$.data.name").value("name"))
        .andExpect(jsonPath("$.data.description").value("desc"))
        .andExpect(jsonPath("$.data.price").value(1L))
        .andExpect(jsonPath("$.data.qty").value(1));

    }

    @Test
    void testDeleteTier() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setUsername("username");

        Event event = new Event();
        event.setMerchant(merchant);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);

        Tier tier = new Tier();
        tier.setId(1L);
        tier.setTicket(ticket);

        when(tierService.findById(1L)).thenReturn(Optional.of(tier));
        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);

        // Act and Assert
        mockMvc.perform(patch("/api/v1/tiers/{id}", 1L)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"))
                .andExpect(jsonPath("$.message").value("Tier deleted successfully"));
    }

    @Test
    void testDeleteTierUnauthorized() throws Exception{
        Merchant merchant = new Merchant();
        merchant.setUsername("test");

        Event event = new Event();
        event.setMerchant(merchant);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);

        Tier tier = new Tier();
        tier.setId(1L);
        tier.setTicket(ticket);

        when(tierService.findById(1L)).thenReturn(Optional.of(tier));
        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);

        // Act and Assert
        mockMvc.perform(patch("/api/v1/tiers/{id}", 1L)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request Not Valid: Unauthorized merchant"));
    }

    @Test
    void testUpdateTier() throws JsonProcessingException, Exception {
            // Create a sample TierRequestDTO
        TierRequestDTO tierRequestDTO = new TierRequestDTO();
        tierRequestDTO.setName("Silver");
        tierRequestDTO.setPrice(10L);
        tierRequestDTO.setQty(50);
        tierRequestDTO.setTicketId(1L);

        // Create HttpHeaders with a valid token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "valid_token");

        // Create a sample TierResponseDTO
        TierResponseDTO tierResponseDTO = new TierResponseDTO();
        tierResponseDTO.setId(1L);
        tierResponseDTO.setName("Silver");
        tierResponseDTO.setQty(10);
        tierResponseDTO.setPrice(50L);

        // Mock the behavior of dependencies
        MerchantResponseDTO merchantResponseDTO = new MerchantResponseDTO();
        merchantResponseDTO.setUsername("test_merchant");
        when(merchantAuthService.decodeToken(anyString())).thenReturn(merchantResponseDTO);
        Event event = new Event();
        event.setId(1L);
        Merchant merchant = new Merchant();
        merchant.setUsername("test_merchant");
        event.setMerchant(merchant);
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);
        when(ticketService.findById(anyLong())).thenReturn(Optional.of(ticket));
        when(tierService.updateTier(eq(tierRequestDTO), anyLong())).thenReturn(tierResponseDTO);

        // Call the controller method and assert the response
        MvcResult result = mockMvc.perform(put("/api/v1/tiers/1")
                .header("Authorization", "valid_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(tierRequestDTO))
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        BaseResponse<TierResponseDTO> response = new ObjectMapper().readValue(responseBody, new TypeReference<>() {});
        assertEquals("00", response.getCode());
        assertEquals("Tier updated successfully", response.getMessage());
        assertEquals(tierResponseDTO, response.getData());
    }
    
}
