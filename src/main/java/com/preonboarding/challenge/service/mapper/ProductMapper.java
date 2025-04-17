package com.preonboarding.challenge.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preonboarding.challenge.entity.*;
import com.preonboarding.challenge.service.dto.ProductDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ProductMapper {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "detail", source = "detail")
    @Mapping(target = "price", expression = "java(mapPriceInfo(product.getPrice()))")
    @Mapping(target = "categories", expression = "java(mapCategories(product))")
    @Mapping(target = "optionGroups", expression = "java(mapOptionGroups(product.getOptionGroups()))")
    @Mapping(target = "images", expression = "java(mapImages(product.getImages()))")
    @Mapping(target = "tags", expression = "java(mapTags(product.getTags()))")
    @Mapping(target = "rating", expression = "java(mapRatingSummary(product.getReviews()))")
    @Mapping(target = "relatedProducts", ignore = true) // 필요시 별도 매핑
    public abstract ProductDto.ProductDetail toProductDetail(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "altText", source = "altText")
    @Mapping(target = "isPrimary", source = "primary")
    @Mapping(target = "displayOrder", source = "displayOrder")
    @Mapping(target = "optionId", expression = "java(image.getOption() != null ? image.getOption().getId() : null)")
    public abstract ProductDto.ImageResponse toImageResponse(ProductImage image);

    // DTO to Entity mappings (new entities)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "optionGroups", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "status", expression = "java(mapProductStatus(request.getStatus()))")
    public abstract Product toProductEntity(ProductDto.CreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "dimensions", expression = "java(convertMapToJsonString(detail.getDimensions()))")
    @Mapping(target = "additionalInfo", expression = "java(convertMapToJsonString(detail.getAdditionalInfo()))")
    public abstract ProductDetail toProductDetailEntity(ProductDto.ProductDetailDto detail, Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    public abstract ProductPrice toProductPriceEntity(ProductDto.ProductPriceDto price, Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "options", ignore = true)
    public abstract ProductOptionGroup toOptionGroupEntity(ProductDto.OptionGroupDto dto, Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "optionGroup", source = "optionGroup")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "displayOrder", source = "dto.displayOrder")
    @Mapping(target = "images", ignore = true)
    public abstract ProductOption toOptionEntity(ProductDto.OptionDto dto, ProductOptionGroup optionGroup);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "displayOrder", source = "dto.displayOrder")
    @Mapping(target = "option", source = "option")
    public abstract ProductImage toImageEntity(ProductDto.ImageRequest dto, Product product, ProductOption option);

    // Update entity mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "detail", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "optionGroups", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "status", expression = "java(request.getStatus() != null ? mapProductStatus(request.getStatus()) : product.getStatus())")
    public abstract void updateProductEntity(ProductDto.UpdateRequest request, @MappingTarget Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "dimensions", expression = "java(detail.getDimensions() != null ? convertMapToJsonString(detail.getDimensions()) : productDetail.getDimensions())")
    @Mapping(target = "additionalInfo", expression = "java(detail.getAdditionalInfo() != null ? convertMapToJsonString(detail.getAdditionalInfo()) : productDetail.getAdditionalInfo())")
    public abstract void updateProductDetailEntity(ProductDto.ProductDetailDto detail, @MappingTarget ProductDetail productDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract void updateProductPriceEntity(ProductDto.ProductPriceDto price, @MappingTarget ProductPrice productPrice);

    // Response mappings
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    public abstract ProductDto.CreateResponse toCreateResponse(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "updatedAt", source = "updatedAt")
    public abstract ProductDto.UpdateResponse toUpdateResponse(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionGroupId", source = "optionGroup.id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "additionalPrice", source = "additionalPrice")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "displayOrder", source = "displayOrder")
    public abstract ProductDto.OptionResponse toOptionResponse(ProductOption option);

    // Helper methods that need to be implemented
    protected ProductDto.PriceInfoDto mapPriceInfo(ProductPrice price) {
        if (price == null) {
            return null;
        }

        return ProductDto.PriceInfoDto.builder()
                .basePrice(price.getBasePrice())
                .salePrice(price.getSalePrice())
                .currency(price.getCurrency())
                .taxRate(price.getTaxRate())
                .discountPercentage(price.getDiscountPercentage())
                .build();
    }

    protected List<ProductDto.CategoryDto> mapCategories(Product product) {
        if (product.getCategories() == null) {
            return List.of();
        }

        return product.getCategories().stream()
                .map(category -> {
                    ProductDto.CategoryDto dto = new ProductDto.CategoryDto();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    dto.setSlug(category.getSlug());

                    if (category.getParent() != null) {
                        ProductDto.ParentCategoryDto parentDto = new ProductDto.ParentCategoryDto();
                        parentDto.setId(category.getParent().getId());
                        parentDto.setName(category.getParent().getName());
                        parentDto.setSlug(category.getParent().getSlug());
                        dto.setParent(parentDto);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected List<ProductDto.OptionGroupResponseDto> mapOptionGroups(List<ProductOptionGroup> groups) {
        if (groups == null) {
            return List.of();
        }

        return groups.stream()
                .map(group -> {
                    ProductDto.OptionGroupResponseDto dto = new ProductDto.OptionGroupResponseDto();
                    dto.setId(group.getId());
                    dto.setName(group.getName());
                    dto.setDisplayOrder(group.getDisplayOrder());

                    List<ProductDto.OptionDto> options = group.getOptions().stream()
                            .map(option -> {
                                ProductDto.OptionDto optionDto = new ProductDto.OptionDto();
                                optionDto.setId(option.getId());
                                optionDto.setOptionGroupId(group.getId());
                                optionDto.setName(option.getName());
                                optionDto.setAdditionalPrice(option.getAdditionalPrice());
                                optionDto.setSku(option.getSku());
                                optionDto.setStock(option.getStock());
                                optionDto.setDisplayOrder(option.getDisplayOrder());
                                return optionDto;
                            })
                            .collect(Collectors.toList());

                    dto.setOptions(options);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected List<ProductDto.ImageResponse> mapImages(List<ProductImage> images) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList());
    }

    protected List<ProductDto.TagDto> mapTags(List<Tag> tags) {
        if (tags == null) {
            return List.of();
        }

        return tags.stream()
                .map(tag -> {
                    ProductDto.TagDto dto = new ProductDto.TagDto();
                    dto.setId(tag.getId());
                    dto.setName(tag.getName());
                    dto.setSlug(tag.getSlug());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    protected ProductDto.RatingSummaryDto mapRatingSummary(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Map<Integer, Integer> distribution = reviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getRating,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        return ProductDto.RatingSummaryDto.builder()
                .average(average)
                .count(reviews.size())
                .distribution(distribution)
                .build();
    }

    protected ProductStatus mapProductStatus(String status) {
        if (status == null) {
            return ProductStatus.ACTIVE;
        }

        return switch (status.toUpperCase()) {
            case "OUT_OF_STOCK" -> ProductStatus.OUT_OF_STOCK;
            case "DELETED" -> ProductStatus.DELETED;
            default -> ProductStatus.ACTIVE;
        };
    }

    protected String convertMapToJsonString(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Map to JSON string", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> convertJsonStringToMap(String json) {
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON string to Map", e);
        }
    }

    // 추가: ProductDetail 직접 매핑을 위한 메서드
    @AfterMapping
    protected void afterMappingProductDetail(Product product, @MappingTarget ProductDto.ProductDetail target) {
        if (product.getDetail() != null) {
            // dimensions와 additionalInfo 필드가 별도 처리되도록 이미 설정됨
            target.getDetail().setWeight(product.getDetail().getWeight());
            target.getDetail().setMaterials(product.getDetail().getMaterials());
            target.getDetail().setCountryOfOrigin(product.getDetail().getCountryOfOrigin());
            target.getDetail().setWarrantyInfo(product.getDetail().getWarrantyInfo());
            target.getDetail().setCareInstructions(product.getDetail().getCareInstructions());
        }
    }
}