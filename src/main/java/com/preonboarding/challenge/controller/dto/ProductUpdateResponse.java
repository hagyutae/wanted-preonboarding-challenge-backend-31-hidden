package com.preonboarding.challenge.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductUpdateResponse(
        Long id,
        String name,
        String slug,
        @JsonProperty("updated_at") String updatedAt
) {}