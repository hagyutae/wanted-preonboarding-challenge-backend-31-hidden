package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}