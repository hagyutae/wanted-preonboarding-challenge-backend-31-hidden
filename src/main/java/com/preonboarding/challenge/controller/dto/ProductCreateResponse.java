package com.preonboarding.challenge.controller.dto;

public record ProductCreateResponse(
        Long id,
        String name,
        String slug,
        String createdAt,
        String updatedAt
) {}