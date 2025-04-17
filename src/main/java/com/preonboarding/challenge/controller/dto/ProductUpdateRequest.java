package com.preonboarding.challenge.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String name;
    private String slug;
    private String shortDescription;
    private String fullDescription;
    private Long sellerId;
    private Long brandId;
    private String status;

    private ProductCreateRequest.ProductDetailDto detail;
    private ProductCreateRequest.ProductPriceDto price;
    private List<Long> categoryIds;
    private List<Long> tagIds;
}