package com.preonboarding.challenge.repository;

import com.preonboarding.challenge.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}