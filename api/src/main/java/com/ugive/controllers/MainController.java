package com.ugive.controllers;

import ch.qos.logback.core.model.Model;
import com.ugive.models.PurchaseOffer;
import com.ugive.models.catalogs.ProductCategory;
import com.ugive.models.catalogs.ProductCondition;
import com.ugive.repositories.PurchaseOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class MainController {
    private final PurchaseOfferRepository purchaseOfferRepository;
    @GetMapping("/")
    public String startPage(Map<String, Object> model) {
        return "start_page";
    }

    /*@GetMapping("/search")
    public List<PurchaseOffer> search(@RequestParam(name="category", required=false) ProductCategory category,
                         @RequestParam(name="condition", required=false) ProductCondition condition,
                         @RequestParam(name="price", required=false) BigDecimal price,
                         Model model) {
        List<PurchaseOffer> products = purchaseOfferRepository.findAllByProductCategoryAndProductConditionAndPrice(category, condition, price);

        return products;
    }*/
}
