package com.ugive.services.catalogs;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.mappers.catalogs.ProductCategoryMapper;
import com.ugive.models.catalogs.ProductCategory;
import com.ugive.repositories.catalogs.ProductCategoryRepository;
import com.ugive.requests.catalogs.ProductCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryMapper categoryMapper;
    private final ProductCategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductCategory create(ProductCategoryRequest categoryRequest) {
        ProductCategory productCategory = categoryMapper.toEntity(categoryRequest);
        return categoryRepository.save(productCategory);
    }

    @Override
    @Transactional
    public ProductCategory update(Integer id, ProductCategoryRequest categoryRequest) {
        ProductCategory productCategory = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found."));
        categoryMapper.updateEntityFromRequest(categoryRequest, productCategory);
        productCategory.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        return categoryRepository.save(productCategory);
    }
}
