package com.ticc.webapiservice.repository;

import com.ticc.webapiservice.entity.PaymentChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentChannelRepository extends JpaRepository<PaymentChannel, Long> {
}
