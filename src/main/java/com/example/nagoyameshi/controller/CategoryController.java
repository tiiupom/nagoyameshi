package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.service.CategoryService;

@Controller
@RequestMapping("/admin/stores")
public class CategoryController {
	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping("/show")
	public String show(Model model) {
		List<Category> category = categoryService.findAllCategories();
		
		model.addAttribute("category", category);
		
		return "admin/stores/show";
	}
}
