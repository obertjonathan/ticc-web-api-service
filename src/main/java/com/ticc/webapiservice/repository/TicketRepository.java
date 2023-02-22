package com.ticc.webapiservice.repository;

import com.ticc.webapiservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByIdAndDeletedAtIsNull(Long id);
}
