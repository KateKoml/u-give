package com.ugive.controllers.catalogs;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.models.catalogs.ProductCategory;
import com.ugive.repositories.catalogs.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/categories")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryRepository productCategoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> findOne(@PathVariable Integer id) {
        ProductCategory category = productCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This category doesn't exists."));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
