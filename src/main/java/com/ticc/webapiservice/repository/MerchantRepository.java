package com.ticc.webapiservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticc.webapiservice.entity.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant,String>{
    Optional<Merchant> findByUsername(String username);

    Optional<Merchant> findByEmail(String email);
}
