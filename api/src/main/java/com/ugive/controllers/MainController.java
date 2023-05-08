package com.ugive.controllers;


import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/search")
    public String search(@RequestParam(name="categoryName") String categoryName,
                                      @RequestParam(name="conditionName") String conditionName,
                                      @RequestParam(name="minPrice") BigDecimal minPrice,
                                      @RequestParam(name="maxPrice") BigDecimal maxPrice,
                                      Model model) {
        if (conditionName.equals("any")) {
            conditionName = "%";
        }
        List<PurchaseOffer> offers = purchaseOfferRepository.findByProductCategoryAndProductConditionAndPrice(categoryName, conditionName, minPrice, maxPrice);
        model.addAttribute("offers", offers);
        return "search-results";
    }
}
