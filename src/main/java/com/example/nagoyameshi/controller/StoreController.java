package com.example.nagoyameshi.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.StoreService;

@Controller
@RequestMapping("/stores")
public class StoreController {
	private final StoreService storeService;
	private final CategoryService categoryService;
	
	public StoreController(StoreService storeService, CategoryService categoryService) {
		this.storeService = storeService;
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@RequestParam(name = "category", required = false) Integer categoryId,
						@RequestParam(name = "price", required = false) Integer priceMin,
						@RequestParam(name = "order", required = false) String order,
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
						Model model)
	{
		Page<Store> storePage;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (order != null && order.equals("PriceMinAsc")) {
				storePage = storeService.findStoresByNamelikeOrAddressLikeOrCategoryNameLikeOrderByPriceMinAsc(keyword, keyword, keyword, pageable);
			} else {
				storePage = storeService.findStoresByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(keyword, keyword, keyword, pageable);
			}
		} else if (categoryId != null) {
			if (order != null && order.equals("priceMinAsc")) {
				storePage = storeService.findStoresByCategoryIdOrderByPriceMinAsc(categoryId, pageable);
			} else {
				storePage = storeService.findStoresByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
			}
		} else if(priceMin != null) {
			if (order != null && order.equals("PriceMinAsc")) {
				storePage = storeService.findStoresByPriceMinThanEqualOrderByPriceMinAsc(priceMin, pageable);
			} else {
				storePage = storeService.findStoresByPriceMinThanEqualOrderByPriceMinAsc(priceMin, pageable);
			}
		} else {
			if (order != null && order.equals("PriceMinAsc")) {
				storePage = storeService.findAllStoresByOrderByPriceMinAsc(pageable);
			} else {
				storePage = storeService.findAllStoresByOrderByCreatedAtDesc(pageable);
			}
		}
		
		/*
			storePage = storeService.findStoreByNameLikeOrAddressLike(keyword, keyword, pageable);
		} else if (categoryId != null && !categoryId.isEmpty()) {
			storePage = storeService.findStoreByCategoryLike(categoryId, pageable);
		} else {
			storePage = storeService.findAllStores(pageable);
		}
		*/
		
		List<Category> categories = categoryService.findAllCategories();
		model.addAttribute("storePage", storePage);
		model.addAttribute("categories", categories);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", categoryId);
		model.addAttribute("priceMin", priceMin);
		model.addAttribute("order", order);
		
		return "stores/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id,
						RedirectAttributes redirectAttributes,
						Model model)
	{
		Optional<Store> optionalStore = storeService.findStoreById(id);
		
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");
			
			return "redirect:/stores";
		}
		
		Store store = optionalStore.get();
		model.addAttribute("store", store);
		
		return "stores/show";
	}
}