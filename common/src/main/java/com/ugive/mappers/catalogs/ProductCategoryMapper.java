package com.ugive.mappers.catalogs;

import com.ugive.models.catalogs.ProductCategory;
import com.ugive.requests.catalogs.ProductCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ProductCategoryMapper {
    private final ModelMapper modelMapper;

    public ProductCategory toEntity(ProductCategoryRequest categoryRequest) {
        return modelMapper.map(categoryRequest, ProductCategory.class);
    }

    public ProductCategory updateEntityFromRequest(ProductCategoryRequest categoryRequest, ProductCategory category) {
        if (categoryRequest.getCategoryName() != null) {
            category.setCategoryName(categoryRequest.getCategoryName());
        }
        category.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        return category;
    }
}
