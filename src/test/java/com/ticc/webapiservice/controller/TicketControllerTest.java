package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.request.TicketRequestDTO;
import com.ticc.webapiservice.dto.response.EventDetailResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.dto.response.TicketResponseDTO;
import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.entity.Ticket;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.service.EventService;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.MerchantService;
import com.ticc.webapiservice.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TicketController.class)
class TicketControllerTest {
    @MockBean
    TicketService ticketService;
    @MockBean
    MerchantAuthService merchantAuthService;
    @MockBean
    EventService eventService;
    @MockBean
    MerchantService merchantService;
    @Autowired
    private MockMvc mockMvc;
    MerchantResponseDTO merchantResponseDTO;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        merchantResponseDTO = new MerchantResponseDTO();
        merchantResponseDTO.setId("merchant1");
        merchantResponseDTO.setName("merchant");
        merchantResponseDTO.setUsername("username");
        merchantResponseDTO.setEmail("email@mail.com");
        merchantResponseDTO.setBio("bio");
        merchantResponseDTO.setLocation("location");

        jwtToken = obtainJwtToken();
    }

    private String obtainJwtToken() {
        return "Bearer fake-token";
    }

    @Test
    void addTicket() throws Exception {
        TicketRequestDTO requestDTO = new TicketRequestDTO();
        TicketResponseDTO responseDTO = new TicketResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.addTicket(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/tickets")
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"name\": \"ticket name\", \"date\": \"03/03/2023\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void updateTicket() throws Exception {
        TicketRequestDTO requestDTO = new TicketRequestDTO();
        TicketResponseDTO responseDTO = new TicketResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.updateTicket(1L, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/tickets/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"name\": \"new ticket name\", \"date\": \"03/03/2023\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void delTicket() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setUsername("username");

        Event event = new Event();
        event.setMerchant(merchant);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);

        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(merchantService.getMerchantByUsername("username")).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketService.delTicket(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/tickets/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void getTicketTiers() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setUsername("username");

        Event event = new Event();
        event.setMerchant(merchant);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);
        ticket.setTiers(List.of(new Tier()));

        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(merchantService.getMerchantByUsername("username")).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/v1/tickets/" + 1 + "/tiers")
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void addTicketNotValid() throws Exception {
        TicketRequestDTO requestDTO = new TicketRequestDTO();
        TicketResponseDTO responseDTO = new TicketResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.addTicket(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/tickets")
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"name\": \"ticket name\", \"date\": \"03/03/2023\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void updateTicketNotValid() throws Exception {
        TicketRequestDTO requestDTO = new TicketRequestDTO();
        TicketResponseDTO responseDTO = new TicketResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.updateTicket(1L, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/tickets/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"name\": \"new ticket name\", \"date\": \"03/03/2023\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void delTicketNotValid() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setUsername("test");

        Event event = new Event();
        event.setMerchant(merchant);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);

        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(merchantService.getMerchantByUsername("test")).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketService.delTicket(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/tickets/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void getTicketTiersNotValid() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setUsername("test");

        Event event = new Event();
        event.setMerchant(merchant);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEvent(event);
        ticket.setTiers(List.of(new Tier()));

        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(merchantService.getMerchantByUsername("test")).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/v1/tickets/" + 1 + "/tiers")
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }
}