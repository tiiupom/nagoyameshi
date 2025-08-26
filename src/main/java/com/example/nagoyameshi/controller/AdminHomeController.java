package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
	private final UserService userService;
	
	public AdminHomeController(UserService userService) {
		this.userService = userService;
	}
	
	// 管理者用の会員情報ページを表示（admin/index.htmlファイル）
	@GetMapping
	public String index(Model model) {
		long totalSubscribers = userService.countUserByRole_Name("ROLE_SUBSCRIBER");
		long salesForThisMonth = 300 * totalSubscribers;
		
		model.addAttribute("salesForThisMonth", salesForThisMonth);
		 
		return "admin/index";
	}
}