package com.ticc.webapiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticc.webapiservice.entity.TransactionTicket;

@Repository
public interface TransactionTicketRepository extends JpaRepository<TransactionTicket,String>{
    
}
