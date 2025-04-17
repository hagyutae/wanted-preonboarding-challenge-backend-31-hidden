package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}