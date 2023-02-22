package com.ticc.webapiservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticc.webapiservice.entity.EventImage;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage,Long>{
    List<EventImage> findByEventIdAndDeletedAtIsNull(Long eventId);

    EventImage findByIdAndDeletedAtIsNull(Long id);
}
