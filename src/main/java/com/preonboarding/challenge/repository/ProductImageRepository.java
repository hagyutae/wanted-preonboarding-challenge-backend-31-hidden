package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}