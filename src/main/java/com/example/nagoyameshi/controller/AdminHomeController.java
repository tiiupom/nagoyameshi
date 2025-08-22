package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.StoreService;
import com.example.nagoyameshi.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
	private final UserService userService;
	private final StoreService storeService;
	private final ReservationService reservationService;
	
	public AdminHomeController(UserService userService, StoreService storeService, ReservationService reservationService) {
		this.userService = userService;
		this.storeService = storeService;
		this.reservationService = reservationService;
	}
	
	// 管理者用の会員情報ページを表示（admin/index.htmlファイル）
	@GetMapping
	public String index(Model model) {
		long totalSubscribers = userService.countUserByRole_Name("ROLE_SUBSCRIBER");
		long salesForThisMonth = 300 * totalSubscribers;
		
		model.addAttribute("salesForThisMonth", salesForThisMonth);
		 
		return "admin/index";
	}
	
	// 管理者用の会員情報ページを表示（users/index.htmlファイル）
	@GetMapping("/users")
	public String userIndex(Model model) {
		long totalGenerals = userService.countUserByRole_Name("ROLE_GENERAL");
		long totalSubscribers = userService.countUserByRole_Name("ROLE_SUBSCRIBER");
		long totalMembers = totalGenerals + totalSubscribers;
		
		model.addAttribute("totalGenerals", totalGenerals);
		model.addAttribute("totalSubscribers", totalSubscribers);
		model.addAttribute("totalMembers", totalMembers);
		
		return "admin/users/index";
	}
	
	// 管理者用の店舗情報ページを表示（stores/index.htmlファイル）
	@GetMapping("/stores")
	public String storeIndex(Model model) {
		long toralStores = storeService.countStores();
		long totalReservations = reservationService.countReservations();
		
		model.addAttribute("toralStores", toralStores);
		model.addAttribute("totalReservations", totalReservations);
		
		return "admin/stores/index";
	}
}