package com.ticc.webapiservice.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.exception.exts.eventimage.EventImageAlreadyExistsException;
import com.ticc.webapiservice.exception.exts.eventimage.EventImageNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ticc.webapiservice.dto.request.EventImageRequestDTO;
import com.ticc.webapiservice.dto.response.EventImageResponseDTO;
import com.ticc.webapiservice.entity.EventImage;
import com.ticc.webapiservice.repository.EventImageRepository;
import com.ticc.webapiservice.service.EventImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventImageServiceImpl implements EventImageService{
    private final EventImageRepository eventImageRepository;
    private final ModelMapper modelMapper;
    private static String notFound = "Image not found";
    private static String cover = "COVER";
    private static String thumbnail = "THUMBNAIL";
    
    @Override
    public EventImageResponseDTO addEventImage(EventImageRequestDTO eventImageRequestDTO) {
        EventImage eventImage = new EventImage();

        Event event = new Event();
        event.setId(eventImageRequestDTO.getEventId());

        eventImage.setEvent(event);
        eventImage.setImageType(eventImageRequestDTO.getImageType());

        if (eventImageRequestDTO.getImageType().equalsIgnoreCase(cover) && !eventImageRepository.findByEventIdAndDeletedAtIsNull(eventImageRequestDTO.getEventId()).stream().filter(e -> e.getImageType().equalsIgnoreCase(cover)).toList().isEmpty()) {
            throw new EventImageAlreadyExistsException("Image type COVER already exists");
        }

        if (eventImageRequestDTO.getImageType().equalsIgnoreCase(thumbnail) && !eventImageRepository.findByEventIdAndDeletedAtIsNull(eventImageRequestDTO.getEventId()).stream().filter(e -> e.getImageType().equalsIgnoreCase(thumbnail)).toList().isEmpty()) {
            throw new EventImageAlreadyExistsException("Image type THUMBNAIL already exists");
        }

        eventImage.setImageLink(eventImageRequestDTO.getImageLink());

        return convertToDto(eventImageRepository.save(eventImage));
    }

    @Override
    public List<EventImageResponseDTO> getEventImageByEventId(Long id) {
        List<EventImage> eventImage = eventImageRepository.findByEventIdAndDeletedAtIsNull(id);
        return eventImage.stream().map(this::convertToDto).toList();
    }

    @Override
    public EventImageResponseDTO updateEventImage(Long id, EventImageRequestDTO eventImageRequestDTO) {
        EventImage eventImage = eventImageRepository.findByIdAndDeletedAtIsNull(id);

        Event event = new Event();
        event.setId(eventImageRequestDTO.getEventId());

        eventImage.setEvent(event);
        eventImage.setImageType(eventImageRequestDTO.getImageType());

        if (eventImageRequestDTO.getImageType().equalsIgnoreCase(cover)) {
            List<EventImage> temp = eventImageRepository.findByEventIdAndDeletedAtIsNull(eventImageRequestDTO.getEventId()).stream().filter(e -> e.getImageType().equalsIgnoreCase(cover)).toList();
            if (!temp.isEmpty() && !temp.get(0).getId().equals(id)) {
                throw new EventImageAlreadyExistsException("Image type COVER already exists");
            }
        }

        if (eventImageRequestDTO.getImageType().equalsIgnoreCase(thumbnail)) {
            List<EventImage> temp = eventImageRepository.findByEventIdAndDeletedAtIsNull(eventImageRequestDTO.getEventId()).stream().filter(e -> e.getImageType().equalsIgnoreCase(thumbnail)).toList();
            if (!temp.isEmpty() && !temp.get(0).getId().equals(id)) {
                throw new EventImageAlreadyExistsException("Image type THUMBNAIL already exists");
            }
        }
        eventImage.setImageLink(eventImageRequestDTO.getImageLink());

        return convertToDto(eventImageRepository.save(eventImage));

    }

    @Override
    public boolean delEventImage(Long id) {
        Optional<EventImage> eventImages = eventImageRepository.findById(id);

        if (eventImages.isEmpty()) {
            throw new EventImageNotFoundException(notFound);
        }

        EventImage eventImage = eventImages.get();
        eventImage.setDeletedAt(LocalDateTime.now());
        eventImageRepository.save(eventImage);

        return true;
    }

    @Override
    public EventImageResponseDTO findById(Long id) {
        EventImage eventImage = eventImageRepository.findById(id).orElseThrow(() -> new EventImageNotFoundException(notFound));
        return convertToDto(eventImage);
    }

    private EventImageResponseDTO convertToDto(EventImage eventImage) {
        return modelMapper.map(eventImage, EventImageResponseDTO.class);
    }
    
}
