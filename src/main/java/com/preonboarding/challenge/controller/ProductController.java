package com.preonboarding.challenge.controller;

import com.preonboarding.challenge.controller.dto.ProductCreateRequest;
import com.preonboarding.challenge.controller.dto.ProductImageRequest;
import com.preonboarding.challenge.controller.dto.ProductOptionRequest;
import com.preonboarding.challenge.controller.dto.ProductUpdateRequest;
import com.preonboarding.challenge.controller.mapper.ProductControllerMapper;
import com.preonboarding.challenge.service.ProductService;
import com.preonboarding.challenge.service.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductControllerMapper mapper;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request) {
        ProductDto.CreateRequest serviceDto = mapper.toServiceDto(request);
        ProductDto.CreateResponse response = productService.createProduct(serviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "상품이 성공적으로 등록되었습니다."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        ProductDto.ProductDetail product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product, "상품 상세 정보를 성공적으로 조회했습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        ProductDto.UpdateRequest serviceDto = mapper.toServiceUpdateDto(request);
        ProductDto.UpdateResponse response = productService.updateProduct(id, serviceDto);
        return ResponseEntity.ok(ApiResponse.success(response, "상품이 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "상품이 성공적으로 삭제되었습니다."));
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<?> addProductOption(
            @PathVariable Long id,
            @RequestParam Long optionGroupId,
            @RequestBody ProductOptionRequest request) {
        ProductDto.OptionRequest serviceDto = mapper.toServiceOptionRequest(request);
        ProductDto.OptionResponse response = productService.addProductOption(id, optionGroupId, serviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "상품 옵션이 성공적으로 추가되었습니다."));
    }

    @PutMapping("/{id}/options/{optionId}")
    public ResponseEntity<?> updateProductOption(
            @PathVariable Long id,
            @PathVariable Long optionId,
            @RequestBody ProductOptionRequest request) {
        ProductDto.OptionRequest serviceDto = mapper.toServiceOptionRequest(request);
        ProductDto.OptionResponse response = productService.updateProductOption(id, optionId, serviceDto);
        return ResponseEntity.ok(ApiResponse.success(response, "상품 옵션이 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{id}/options/{optionId}")
    public ResponseEntity<?> deleteProductOption(@PathVariable Long id, @PathVariable Long optionId) {
        productService.deleteProductOption(id, optionId);
        return ResponseEntity.ok(ApiResponse.success(null, "상품 옵션이 성공적으로 삭제되었습니다."));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<?> addProductImage(@PathVariable Long id, @RequestBody ProductImageRequest request) {
        ProductDto.ImageRequest serviceDto = mapper.toServiceImageRequest(request);
        ProductDto.ImageResponse response = productService.addProductImage(id, serviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "상품 이미지가 성공적으로 추가되었습니다."));
    }
}
