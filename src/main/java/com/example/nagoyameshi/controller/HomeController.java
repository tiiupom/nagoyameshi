package com.example.nagoyameshi.controller;
 
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.security.UserDetailsImpl;

@Controller
public class HomeController {
	@GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
    	
    	// 現在ログイン中のユーザーが管理者であれば管理者用のトップページにリダイレクト
    	if (userDetailsImpl != null && userDetailsImpl.getUser().getRole().getName().equals("ROLE_ADMIN")) {
    		return "redirect:/admin";
    	}
    	
        return "index";
    }
}