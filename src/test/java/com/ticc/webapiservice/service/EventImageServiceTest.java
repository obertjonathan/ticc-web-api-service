package com.ticc.webapiservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.ticc.webapiservice.dto.response.EventImageResponseDTO;
import com.ticc.webapiservice.entity.Category;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.exception.exts.eventimage.EventImageAlreadyExistsException;
import com.ticc.webapiservice.exception.exts.eventimage.EventImageNotFoundException;
import com.ticc.webapiservice.service.implementation.EventImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.ticc.webapiservice.dto.request.EventImageRequestDTO;
import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.entity.EventImage;
import com.ticc.webapiservice.repository.EventImageRepository;

@ExtendWith(MockitoExtension.class)
public class EventImageServiceTest {
    @Mock
    private EventImageRepository eventImageRepository;
    @InjectMocks
    private EventImageServiceImpl eventImageService;

    ModelMapper modelMapper = Mockito.spy(ModelMapper.class);
    @Mock
    private EventImage eventImage;
    @Mock
    private Event event;
    @Mock
    private Category category;
    @Mock
    private Merchant merchant;
    private EventImageRequestDTO eventImageRequestDTO;
    private EventImageRequestDTO eventImageRequestCover;
    private EventImageRequestDTO eventImageRequestThumbnail;
    private EventImageResponseDTO eventImageResponseDTO;

    @BeforeEach
    void setUp() {
        eventImageService = new EventImageServiceImpl(eventImageRepository, modelMapper);

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

        eventImageRequestDTO = new EventImageRequestDTO();
        eventImageRequestDTO.setEventId(1L);
        eventImageRequestDTO.setImageType("IMAGE");
        eventImageRequestDTO.setImageLink("image.link");

        eventImageRequestCover = new EventImageRequestDTO();
        eventImageRequestCover.setEventId(1L);
        eventImageRequestCover.setImageType("COVER");
        eventImageRequestCover.setImageLink("image.link");

        eventImageRequestThumbnail = new EventImageRequestDTO();
        eventImageRequestThumbnail.setEventId(1L);
        eventImageRequestThumbnail.setImageType("THUMBNAIL");
        eventImageRequestThumbnail.setImageLink("image.link");

        eventImageResponseDTO = new EventImageResponseDTO();
        eventImageResponseDTO.setId(1L);
        eventImageResponseDTO.setImageType("COVER");
        eventImageResponseDTO.setImageLink("image.link");
    }

    @Test
    void testAddEventImage() {
        when(eventImage.getEvent()).thenReturn(event);
        when(event.getId()).thenReturn(1L);
        when(eventImage.getImageType()).thenReturn("IMAGE");
        when(eventImage.getImageLink()).thenReturn("image.link");
        when(eventImageRepository.save(any())).thenReturn(eventImage);

        EventImageResponseDTO responseDTO = eventImageService.addEventImage(eventImageRequestDTO);

        assertEquals(responseDTO.getImageType(), eventImageRequestDTO.getImageType());
        assertEquals(responseDTO.getImageLink(), eventImageRequestDTO.getImageLink());
    }

    @Test
    void testGetEventImageByEventId() {
        when(event.getId()).thenReturn(1L);
        when(eventImageRepository.findByEventIdAndDeletedAtIsNull(any())).thenReturn(List.of(eventImage));
        when(eventImage.getEvent()).thenReturn(event);

        when(eventImage.getImageType()).thenReturn("IMAGE");
        when(eventImage.getImageLink()).thenReturn("image.link");

        List<EventImageResponseDTO> responseDTO = eventImageService.getEventImageByEventId(any());

        assertEquals(responseDTO.get(0).getImageLink(), eventImageRequestDTO.getImageLink());
    }

    @Test
    void testUpdateEventImage() {
        when(eventImage.getEvent()).thenReturn(event);
        when(event.getId()).thenReturn(1L);
        when(eventImageRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(eventImage);
        when(eventImage.getImageType()).thenReturn("IMAGE");
        when(eventImage.getImageLink()).thenReturn("image.link");
        when(eventImageRepository.save(any())).thenReturn(eventImage);

        EventImageResponseDTO responseDTO = eventImageService.updateEventImage(any(), eventImageRequestDTO);

        assertEquals(responseDTO.getImageType(), eventImageRequestDTO.getImageType());
        assertEquals(responseDTO.getImageLink(), eventImageRequestDTO.getImageLink());
    }

    @Test
    void delEventImage() {
        when(eventImageRepository.findById(any())).thenReturn(Optional.of(eventImage));

        eventImageService.delEventImage(any());
    }

    @Test
    void testFindById() {
        when(eventImageRepository.findById(any())).thenReturn(Optional.of(eventImage));
        when(eventImage.getId()).thenReturn(1L);
        when(eventImage.getImageLink()).thenReturn("image.link");

        EventImageResponseDTO responseDTO = eventImageService.findById(any());

        assertEquals(responseDTO.getImageLink(), eventImageRequestDTO.getImageLink());
    }

    @Test
    void testAddEventImageCoverExists() {
        when(eventImageRepository.findByEventIdAndDeletedAtIsNull(any())).thenReturn(List.of(eventImage));
        when(eventImage.getImageType()).thenReturn("COVER");

        assertThrows(EventImageAlreadyExistsException.class, () -> eventImageService.addEventImage(eventImageRequestCover));
    }

    @Test
    void testAddEventImageThumbnailExists() {
        when(eventImageRepository.findByEventIdAndDeletedAtIsNull(any())).thenReturn(List.of(eventImage));
        when(eventImage.getImageType()).thenReturn("THUMBNAIL");

        assertThrows(EventImageAlreadyExistsException.class, () -> eventImageService.addEventImage(eventImageRequestThumbnail));
    }

    @Test
    void testUpdateEventImageCoverExists() {
        when(eventImageRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(eventImage);
        when(eventImage.getImageType()).thenReturn("COVER");

        when(eventImageRepository.findByEventIdAndDeletedAtIsNull(any())).thenReturn(List.of(eventImage));
        when(eventImage.getImageType()).thenReturn("COVER");

        assertThrows(EventImageAlreadyExistsException.class, () -> eventImageService.updateEventImage(any(), eventImageRequestCover));
    }

    @Test
    void testUpdateEventImageThumbnailExists() {
        when(eventImageRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(eventImage);
        when(eventImage.getImageType()).thenReturn("THUMBNAIL");

        when(eventImageRepository.findByEventIdAndDeletedAtIsNull(any())).thenReturn(List.of(eventImage));
        when(eventImage.getImageType()).thenReturn("THUMBNAIL");

        assertThrows(EventImageAlreadyExistsException.class, () -> eventImageService.updateEventImage(any(), eventImageRequestThumbnail));
    }

    @Test
    void testDelEventImageNotFound() {
        when(eventImageRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EventImageNotFoundException.class, () -> eventImageService.delEventImage(any()));
    }

    @Test
    void testFindByIdNotFound() {
        when(eventImageRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EventImageNotFoundException.class, () -> eventImageService.findById(any()));
    }

}
