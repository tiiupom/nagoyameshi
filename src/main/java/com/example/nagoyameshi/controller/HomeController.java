package com.example.nagoyameshi.controller;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.service.StoreService;

@Controller
public class HomeController {
	private final StoreService storeService;
	
	public HomeController(StoreService storeService) {
		this.storeService = storeService;
	}
	
	@GetMapping("/")
    public String index(Model model) {
    	Page<Store> highlyRatedStores = storeService.findAllStoresByOrderByAverageScoreDesc(PageRequest.of(0,6));
    	Page<Store> newStores = storeService.findAllStoresByOrderByCreatedAtDesc(PageRequest.of(0, 6));
    	
    	model.addAttribute("highlyRatedStores", highlyRatedStores);
    	model.addAttribute("newStores", newStores);
    	
    	return "index";
    }
}