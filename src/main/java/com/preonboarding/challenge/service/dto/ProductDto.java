package com.preonboarding.challenge.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDto {

    // Request DTOs
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private Long sellerId;
        private Long brandId;
        private String status; // ACTIVE, OUT_OF_STOCK, DELETED

        private ProductDetailDto detail;
        private ProductPriceDto price;
        @Builder.Default
        private List<Long> categoryIds = new ArrayList<>();
        @Builder.Default
        private List<OptionGroupDto> optionGroups = new ArrayList<>();
        @Builder.Default
        private List<ImageRequest> images = new ArrayList<>();
        @Builder.Default
        private List<Long> tagIds = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private Long sellerId;
        private Long brandId;
        private String status;

        private ProductDetailDto detail;
        private ProductPriceDto price;
        private List<Long> categoryIds;
        private List<Long> tagIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDetailDto {
        private Double weight;
        private Map<String, Object> dimensions; // JSON: {"width": float, "height": float, "depth": float}
        private String materials;
        private String countryOfOrigin;
        private String warrantyInfo;
        private String careInstructions;
        private Map<String, Object> additionalInfo; // JSON object for additional information
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductPriceDto {
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private BigDecimal costPrice;
        @Builder.Default
        private String currency = "KRW";
        private BigDecimal taxRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionGroupDto {
        private String name;
        private Integer displayOrder;
        @Builder.Default
        private List<OptionDto> options = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionRequest {
        private String name;
        private BigDecimal additionalPrice;
        private String sku;
        private Integer stock;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionDto {
        private Long id;
        private Long optionGroupId; // 명시적으로 추가
        private String name;
        private BigDecimal additionalPrice;
        private String sku;
        private Integer stock;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageRequest {
        private String url;
        private String altText;
        private boolean isPrimary;
        private Integer displayOrder;
        private Long optionId;
    }

    // Response DTOs
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateResponse {
        private Long id;
        private String name;
        private String slug;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateResponse {
        private Long id;
        private String name;
        private String slug;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionResponse {
        private Long id;
        private Long optionGroupId;
        private String name;
        private BigDecimal additionalPrice;
        private String sku;
        private Integer stock;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageResponse {
        private Long id;
        private String url;
        private String altText;
        private boolean isPrimary;
        private Integer displayOrder;
        private Long optionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSummary {
        private Long id;
        private String name;
        private String slug;
        private String shortDescription;
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private String currency;
        private ImageDto primaryImage;
        private BrandDto brand;
        private SellerDto seller;
        private Double rating;
        private Integer reviewCount;
        private boolean inStock;
        private String status;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDetail {
        private Long id;
        private String name;
        private String slug;
        private String shortDescription;
        private String fullDescription;
        private SellerDto seller;
        private BrandDto brand;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private ProductDetailDto detail;
        private PriceInfoDto price;
        private List<CategoryDto> categories;
        private List<OptionGroupResponseDto> optionGroups;
        private List<ImageResponse> images;
        private List<TagDto> tags;
        private RatingSummaryDto rating;
        private List<ProductSummary> relatedProducts;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageDto {
        private String url;
        private String altText;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BrandDto {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SellerDto {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryDto {
        private Long id;
        private String name;
        private String slug;
        private ParentCategoryDto parent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParentCategoryDto {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriceInfoDto {
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private String currency;
        private BigDecimal taxRate;
        private Integer discountPercentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionGroupResponseDto {
        private Long id;
        private String name;
        private Integer displayOrder;
        private List<OptionDto> options;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TagDto {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RatingSummaryDto {
        private Double average;
        private Integer count;
        private Map<Integer, Integer> distribution; // Map rating -> count (e.g., {5: 95, 4: 20, ...})
    }
}