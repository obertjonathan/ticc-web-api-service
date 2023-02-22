package com.ticc.webapiservice.repository;

import com.ticc.webapiservice.entity.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndDeletedAtIsNull(Long id);
}
