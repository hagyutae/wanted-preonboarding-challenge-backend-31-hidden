package com.preonboarding.challenge.service;

import com.preonboarding.challenge.service.dto.ProductDto;

public interface ProductService {

    // 상품 관리 API
    ProductDto.CreateResponse createProduct(ProductDto.CreateRequest request);

    ProductDto.ProductDetail getProductById(Long productId);

    ProductDto.UpdateResponse updateProduct(Long productId, ProductDto.UpdateRequest request);

    void deleteProduct(Long productId);

    // 옵션 관리
    ProductDto.OptionResponse addProductOption(Long productId, Long optionGroupId, ProductDto.OptionRequest option);

    ProductDto.OptionResponse updateProductOption(Long productId, Long optionId, ProductDto.OptionRequest option);

    void deleteProductOption(Long productId, Long optionId);

    // 이미지 관리
    ProductDto.ImageResponse addProductImage(Long productId, ProductDto.ImageRequest image);
}