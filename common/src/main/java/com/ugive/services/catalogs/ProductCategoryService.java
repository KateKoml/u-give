package com.ugive.services.catalogs;

import com.ugive.models.catalogs.ProductCategory;
import com.ugive.requests.catalogs.ProductCategoryRequest;

public interface ProductCategoryService {
    ProductCategory create(ProductCategoryRequest categoryRequest);
    ProductCategory update(Integer id, ProductCategoryRequest categoryRequest);
}
