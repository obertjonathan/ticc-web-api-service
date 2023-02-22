package com.ticc.webapiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticc.webapiservice.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
  