package com.ticc.webapiservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticc.webapiservice.entity.Tier;

@Repository
public interface TierRepository extends JpaRepository<Tier,Long>{

    Optional<Tier> findByIdAndDeletedAtIsNull(Long id);
}
