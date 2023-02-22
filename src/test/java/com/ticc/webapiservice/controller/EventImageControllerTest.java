package com.ticc.webapiservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ticc.webapiservice.dto.response.EventDetailResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.service.EventService;
import com.ticc.webapiservice.service.MerchantAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ticc.webapiservice.dto.request.EventImageRequestDTO;
import com.ticc.webapiservice.dto.response.EventImageResponseDTO;
import com.ticc.webapiservice.service.EventImageService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(EventImageController.class)
public class EventImageControllerTest {

    @MockBean
    EventImageService eventImageService;
    @MockBean
    MerchantAuthService merchantAuthService;
    @MockBean
    EventService eventService;
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
    void testAddEventImage() throws Exception {
        EventImageRequestDTO requestDTO = new EventImageRequestDTO();
        EventImageResponseDTO responseDTO = new EventImageResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventImageService.addEventImage(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/event-images")
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"image_type\": \"COVER\", \"image_link\": \"image.png\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));

    }

    @Test
    void testGetEventImage() throws Exception {
        EventDetailResponseDTO responseDTO = new EventDetailResponseDTO();
        responseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/event-images/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testUpdateEventImage() throws Exception {
        EventImageRequestDTO requestDTO = new EventImageRequestDTO();
        EventImageResponseDTO responseDTO = new EventImageResponseDTO();
        responseDTO.setEventId(1L);
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventImageService.findById(1L)).thenReturn(responseDTO);
        when(eventImageService.updateEventImage(1L, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/event-images/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"image_type\": \"IMAGE1\", \"image_link\": \"image1.png\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testDelEventImage() throws Exception {
        EventImageResponseDTO responseDTO = new EventImageResponseDTO();
        responseDTO.setEventId(1L);
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("username");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventImageService.findById(1L)).thenReturn(responseDTO);
        when(eventImageService.delEventImage(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/event-images/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void testAddEventImageNotValid() throws Exception {
        EventImageRequestDTO requestDTO = new EventImageRequestDTO();
        EventImageResponseDTO responseDTO = new EventImageResponseDTO();
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventImageService.addEventImage(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/event-images")
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"image_type\": \"COVER\", \"image_link\": \"image.png\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));

    }

    @Test
    void testGetEventImageNotValid() throws Exception {
        EventDetailResponseDTO responseDTO = new EventDetailResponseDTO();
        responseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/event-images/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void testUpdateEventImageNotValid() throws Exception {
        EventImageRequestDTO requestDTO = new EventImageRequestDTO();
        EventImageResponseDTO responseDTO = new EventImageResponseDTO();
        responseDTO.setEventId(1L);
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventImageService.findById(1L)).thenReturn(responseDTO);
        when(eventImageService.updateEventImage(1L, requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/event-images/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{ \"event_id\": \"1\", \"image_type\": \"IMAGE1\", \"image_link\": \"image1.png\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }

    @Test
    void testDelEventImageNotValid() throws Exception {
        EventImageResponseDTO responseDTO = new EventImageResponseDTO();
        responseDTO.setEventId(1L);
        EventDetailResponseDTO detailResponseDTO = new EventDetailResponseDTO();
        detailResponseDTO.setMerchantUsername("test");

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(eventService.findById(1L)).thenReturn(detailResponseDTO);
        when(eventImageService.findById(1L)).thenReturn(responseDTO);
        when(eventImageService.delEventImage(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/v1/event-images/" + 1)
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }
}
