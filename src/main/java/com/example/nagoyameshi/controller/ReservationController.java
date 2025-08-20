package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;

import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.StoreService;

@Controller
public class ReservationController {
	private final ReservationService reservationService;
	private final StoreService storeService;
	
	public ReservationController(ReservationService reservationService, StoreService storeService) {
		this.reservationService = reservationService;
		this.storeService = storeService;
	}
	
	
}
