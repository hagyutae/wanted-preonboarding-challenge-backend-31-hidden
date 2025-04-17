package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}