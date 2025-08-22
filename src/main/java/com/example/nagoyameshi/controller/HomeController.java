package com.example.nagoyameshi.controller;
 
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.security.UserDetailsImpl;
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
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
    	
    	// 現在ログイン中のユーザーが管理者であれば管理者用のトップページにリダイレクト
    	if (userDetailsImpl != null && userDetailsImpl.getUser().getRole().getName().equals("ROLE_ADMIN")) {
    		return "redirect:/admin";
    	}
    	
    	Page<Store> highlyRatedStores = storeService.findAllStoresByOrderByAverageScoreDesc(PageRequest.of(0,6));
    	Page<Store> newStores = storeService.findAllStoresByOrderByCreatedAtDesc(PageRequest.of(0,6));
    	Category chuka = categoryService.findFirstCategoryByName("中華");
    	Category kaisen = categoryService.findFirstCategoryByName("海鮮");
    	Category pan = categoryService.findFirstCategoryByName("パン");
    	Category pasta = categoryService.findFirstCategoryByName("パスタ");
    	Category piza = categoryService.findFirstCategoryByName("ピザ");
    	Category ramen = categoryService.findFirstCategoryByName("ラーメン");
    	Category sobaudon = categoryService.findFirstCategoryByName("そば・うどん");
    	Category wasyoku = categoryService.findFirstCategoryByName("和食");
    	Category yosyoku = categoryService.findFirstCategoryByName("洋食");
    	List<Category> categories = categoryService.findAllCategories();
    	
    	model.addAttribute("highlyRatedStores", highlyRatedStores);
    	model.addAttribute("newStores", newStores);
    	model.addAttribute("chuka", chuka);
    	model.addAttribute("kaisen", kaisen);
    	model.addAttribute("pan", pan);
    	model.addAttribute("pasta", pasta);
    	model.addAttribute("piza", piza);
    	model.addAttribute("ramen", ramen);
    	model.addAttribute("sobaudon", sobaudon);
    	model.addAttribute("wasyoku", wasyoku);
    	model.addAttribute("yosyoku", yosyoku);
    	model.addAttribute("categories", categories);
    	
        return "index";
    }
}