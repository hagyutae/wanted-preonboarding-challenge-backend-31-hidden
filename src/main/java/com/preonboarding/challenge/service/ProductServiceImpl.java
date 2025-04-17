package com.preonboarding.challenge.service;

import com.preonboarding.challenge.entity.*;
import com.preonboarding.challenge.exception.ResourceNotFoundException;
import com.preonboarding.challenge.repository.*;
import com.preonboarding.challenge.service.dto.ProductDto;
import com.preonboarding.challenge.service.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;
    private final TagRepository tagRepository;
    private final ProductOptionGroupRepository optionGroupRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductImageRepository imageRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto.CreateResponse createProduct(ProductDto.CreateRequest request) {
        // 1. 기본 Product 엔티티 생성
        Product product = productMapper.toProductEntity(request);

        // 2. 연관 엔티티 설정
        if (request.getSellerId() != null) {
            Seller seller = sellerRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller", request.getSellerId()));
            product.setSeller(seller);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", request.getBrandId()));
            product.setBrand(brand);
        }

        // 3. 저장 및 ID 획득
        product = productRepository.save(product);

        // 4. 연관 관계 설정 및 저장
        // ProductDetail 생성 및 저장
        if (request.getDetail() != null) {
            ProductDetail detail = productMapper.toProductDetailEntity(request.getDetail(), product);
            product.setDetail(detail);
        }

        // ProductPrice 생성 및 저장
        if (request.getPrice() != null) {
            ProductPrice price = productMapper.toProductPriceEntity(request.getPrice(), product);
            product.setPrice(price);
        }

        // 카테고리 연결
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            product.getCategories().addAll(categories);
        }

        // 태그 연결
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            product.getTags().addAll(tags);
        }

        // 옵션 그룹 및 옵션 생성
        if (request.getOptionGroups() != null) {
            for (ProductDto.OptionGroupDto groupDto : request.getOptionGroups()) {
                ProductOptionGroup group = productMapper.toOptionGroupEntity(groupDto, product);
                product.getOptionGroups().add(group);

                // 옵션 생성
                if (groupDto.getOptions() != null) {
                    for (ProductDto.OptionDto optionDto : groupDto.getOptions()) {
                        ProductOption option = productMapper.toOptionEntity(optionDto, group);
                        group.getOptions().add(option);
                    }
                }
            }
        }

        // 이미지 생성
        if (request.getImages() != null) {
            for (ProductDto.ImageRequest imageDto : request.getImages()) {
                ProductOption option = null;
                if (imageDto.getOptionId() != null) {
                    option = optionRepository.findById(imageDto.getOptionId())
                            .orElse(null);
                }
                ProductImage image = productMapper.toImageEntity(imageDto, product, option);
                product.getImages().add(image);
            }
        }

        // 최종 저장 및 응답 생성
        product = productRepository.save(product);
        return productMapper.toCreateResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto.ProductDetail getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        return productMapper.toProductDetail(product);
    }

    @Override
    @Transactional
    public ProductDto.UpdateResponse updateProduct(Long productId, ProductDto.UpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // 기본 필드 업데이트
        productMapper.updateProductEntity(request, product);

        // 연관 엔티티 업데이트
        if (request.getSellerId() != null) {
            Seller seller = sellerRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller", request.getSellerId()));
            product.setSeller(seller);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", request.getBrandId()));
            product.setBrand(brand);
        }

        // ProductDetail 업데이트
        if (request.getDetail() != null && product.getDetail() != null) {
            productMapper.updateProductDetailEntity(request.getDetail(), product.getDetail());
        }

        // ProductPrice 업데이트
        if (request.getPrice() != null && product.getPrice() != null) {
            productMapper.updateProductPriceEntity(request.getPrice(), product.getPrice());
        }

        // 카테고리 업데이트
        if (request.getCategoryIds() != null) {
            product.getCategories().clear();
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            product.getCategories().addAll(categories);
        }

        // 태그 업데이트
        if (request.getTagIds() != null) {
            product.getTags().clear();
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            product.getTags().addAll(tags);
        }

        // 저장 및 응답 생성
        product = productRepository.save(product);
        return productMapper.toUpdateResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // 소프트 삭제
        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);

        // 하드 삭제가 필요한 경우 사용
        // productRepository.delete(product);
    }

    @Override
    @Transactional
    public ProductDto.OptionResponse addProductOption(Long productId, Long optionGroupId, ProductDto.OptionRequest request) {
        ProductOptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("OptionGroup", optionGroupId));

        // 해당 옵션 그룹이 요청된 상품에 속하는지 확인
        if (!optionGroup.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("OptionGroup does not belong to the specified product");
        }

        // OptionDto 생성 및 변환
        ProductDto.OptionDto optionDto = ProductDto.OptionDto.builder()
                .optionGroupId(optionGroupId)
                .name(request.getName())
                .additionalPrice(request.getAdditionalPrice())
                .sku(request.getSku())
                .stock(request.getStock())
                .displayOrder(request.getDisplayOrder())
                .build();

        // 옵션 엔티티 생성 및 저장
        ProductOption option = productMapper.toOptionEntity(optionDto, optionGroup);
        option = optionRepository.save(option);

        return productMapper.toOptionResponse(option);
    }

    @Override
    @Transactional
    public ProductDto.OptionResponse updateProductOption(Long productId, Long optionId, ProductDto.OptionRequest request) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("Option", optionId));

        // 해당 옵션이 요청된 상품에 속하는지 확인
        if (!option.getOptionGroup().getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the specified product");
        }

        // 옵션 업데이트
        if (request.getName() != null) {
            option.setName(request.getName());
        }

        if (request.getAdditionalPrice() != null) {
            option.setAdditionalPrice(request.getAdditionalPrice());
        }

        if (request.getSku() != null) {
            option.setSku(request.getSku());
        }

        if (request.getStock() != null) {
            option.setStock(request.getStock());
        }

        if (request.getDisplayOrder() != null) {
            option.setDisplayOrder(request.getDisplayOrder());
        }

        option = optionRepository.save(option);
        return productMapper.toOptionResponse(option);
    }

    @Override
    @Transactional
    public void deleteProductOption(Long productId, Long optionId) {
        ProductOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("Option", optionId));

        // 해당 옵션이 요청된 상품에 속하는지 확인
        if (!option.getOptionGroup().getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the specified product");
        }

        optionRepository.delete(option);
    }

    @Override
    @Transactional
    public ProductDto.ImageResponse addProductImage(Long productId, ProductDto.ImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        ProductOption option = null;
        if (request.getOptionId() != null) {
            option = optionRepository.findById(request.getOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Option", request.getOptionId()));

            // 해당 옵션이 요청된 상품에 속하는지 확인
            if (!option.getOptionGroup().getProduct().getId().equals(productId)) {
                throw new IllegalArgumentException("Option does not belong to the specified product");
            }
        }

        // 이미지 엔티티 생성 및 저장
        ProductImage image = productMapper.toImageEntity(request, product, option);
        image = imageRepository.save(image);

        return productMapper.toImageResponse(image);
    }
}