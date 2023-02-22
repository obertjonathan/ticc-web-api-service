package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.request.EventRequestDTO;
import com.ticc.webapiservice.dto.response.*;
import com.ticc.webapiservice.service.EventService;
import com.ticc.webapiservice.service.MerchantAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {
    @MockBean
    EventService eventService;
    @MockBean
    MerchantAuthService merchantAuthService;
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
    void testAddEvent() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        EventResponseDTO responseDTO = new EventResponseDTO();

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.addEvent(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/events/")
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"merchant_username\": \"username\", \"category_id\": \"1\", \"name\": \"event name\", \"date\": \"12/12/2022\", \"sell_start_date\": \"11/11/2022\", \"sell_end_date\": \"12/12/2022\", \"description\": \"event desc\", \"location\": \"event loc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testDelEvent() throws Exception {
        EventDetailResponseDTO responseDTO = new EventDetailResponseDTO();
        responseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(responseDTO);
        when(eventService.delEvent(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/events/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testFindById() throws Exception {
        EventDetailResponseDTO responseDTO = new EventDetailResponseDTO();
        responseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/events/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testUpdateEvent() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        EventResponseDTO responseDTO = new EventResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventService.updateEvent(1L, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/events/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"merchant_username\": \"username\", \"category_id\": \"1\", \"name\": \"new event name\", \"date\": \"12/12/2022\", \"sell_start_date\": \"11/11/2022\", \"sell_end_date\": \"12/12/2022\", \"description\": \"event desc\", \"location\": \"event loc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testAddEventNotValid() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        EventResponseDTO responseDTO = new EventResponseDTO();

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.addEvent(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/events/")
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"merchant_username\": \"test\", \"category_id\": \"1\", \"name\": \"event name\", \"date\": \"12/12/2022\", \"sell_start_date\": \"11/11/2022\", \"sell_end_date\": \"12/12/2022\", \"description\": \"event desc\", \"location\": \"event loc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void testDelEventNotValid() throws Exception {
        EventDetailResponseDTO responseDTO = new EventDetailResponseDTO();
        responseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(responseDTO);
        when(eventService.delEvent(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/events/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void testFindByIdNotValid() throws Exception {
        EventDetailResponseDTO responseDTO = new EventDetailResponseDTO();
        responseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/events/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void testUpdateEventNotValid() throws Exception {
        EventRequestDTO requestDTO = new EventRequestDTO();
        EventResponseDTO responseDTO = new EventResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventService.updateEvent(1L, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/events/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"merchant_username\": \"test\", \"category_id\": \"1\", \"name\": \"new event name\", \"date\": \"12/12/2022\", \"sell_start_date\": \"11/11/2022\", \"sell_end_date\": \"12/12/2022\", \"description\": \"event desc\", \"location\": \"event loc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }
}
