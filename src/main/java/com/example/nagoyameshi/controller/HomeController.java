package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.StoreService;

@Controller
public class HomeController {
    private final StoreService storeService;
    private final CategoryService categoryService;

    public HomeController(StoreService storeService, CategoryService categoryService) {
        this.storeService = storeService;
        this.categoryService = categoryService;
    }
    
    @GetMapping("/")
    public String index(Model model) {
    	Page<Store> highlyRatedStores = storeService.findAllStoresByOrderByAverageScoreDesc(PageRequest.of(0, 6));
        Category washoku = categoryService.findFirstCategoryByName("和食");
        Category yosyoku = categoryService.findFirstCategoryByName("洋食");
        Category chuka = categoryService.findFirstCategoryByName("中華");
        Category kaisen = categoryService.findFirstCategoryByName("魚介・海鮮料理");
        Category pasta = categoryService.findFirstCategoryByName("パスタ");
        Category piza = categoryService.findFirstCategoryByName("ピザ");
        Category pan = categoryService.findFirstCategoryByName("パン");
        Category ramen = categoryService.findFirstCategoryByName("ラーメン");
        Category sobaudon = categoryService.findFirstCategoryByName("そば・うどん");
        List<Category> categories = categoryService.findAllCategories();

        model.addAttribute("highlyRatedStores", highlyRatedStores);
        model.addAttribute("washoku", washoku);
        model.addAttribute("yosyoku", yosyoku);
        model.addAttribute("chuka", chuka);
        model.addAttribute("kaisen", kaisen);
        model.addAttribute("pasta", pasta);
        model.addAttribute("piza", piza);
        model.addAttribute("pan", pan);
        model.addAttribute("ramen", ramen);
        model.addAttribute("sobaudon", sobaudon);
        model.addAttribute("categories", categories);        
        
        return "index";
    }
}