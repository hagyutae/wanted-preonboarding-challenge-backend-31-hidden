package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}