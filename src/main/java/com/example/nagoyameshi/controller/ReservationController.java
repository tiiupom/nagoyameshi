package com.example.nagoyameshi.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
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
	
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
						RedirectAttributes redirectAttributes,
						Model model)
	{
		User user = userDetailsImpl.getUser();
		
		if (user.getRole().getName().equals("ROLE_GENERAL")) {
			redirectAttributes.addFlashAttribute("subscriptionMessage", "この操作を利用するには有料プランへの登録が必要です");
			
			return "redirect:/subscription/register";
		}
		
		Page<Reservation> reservationPage = reservationService.findReservationsByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("reservationPage", reservationPage);
		model.addAttribute("currentDateTime", LocalDateTime.now());
		
		return "reservations/index";
	}
	
	@GetMapping("/stores/{storeId}/reservations/register")
	public String register(@PathVariable(name = "storeId") Integer storeId,
							@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
							RedirectAttributes redirectAttributes,
							Model model)
	{
		User user = userDetailsImpl.getUser();
		
		if (user.getRole().getName().equals("ROLE_GENERAL")) {
			redirectAttributes.addFlashAttribute("subscriptionMessage", "この操作を利用するには有料プランへの登録が必要です");
			
			return "redirect:/subscription/register";
		}
		
		Optional<Store> optionalStore = storeService.findStoreById(storeId);
		
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません");
			
			return "redirect:/stores";
		}
		
		Store store = optionalStore.get();
		
		model.addAttribute("store", store);
		model.addAttribute("reservationInputForm", new ReservationInputForm());
		
		return "reservations/register";
	}
	
	@PostMapping("/stores/{storeId}/reservations/create")
	public String create(@PathVariable(name = "storeId") Integer storeId,
			@ModelAttribute @Validated ReservationInputForm reservationInputForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes,
			Model model)
	{
		User user = userDetailsImpl.getUser();
		
		if (user.getRole().getName().equals("ROLE_GENERAL")) {
			redirectAttributes.addFlashAttribute("subscriptionMessage", "この操作を利用するには有料プランへの登録が必要です");
			
			return "redirect:/subscription/register";
		}
		
		Optional<Store> optionalStore = storeService.findStoreById(storeId);
		
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません");
			
			return "redirect:/stores";
		}
		
		LocalDate reservationDate = reservationInputForm.getReservationDate();
		LocalTime reservationTime = reservationInputForm.getReservationTime();
		
		if (reservationDate != null && reservationTime != null) {
			LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);
			
			if (!reservationService.isAtLeastTwoHoursInFuture(reservationDateTime)) {
				FieldError fieldError = new FieldError(bindingResult.getObjectName(), "reservationTime", "当日の予約は30分前までにお願いいたします。");
				bindingResult.addError(fieldError);
			}
		}
		    
	    Store store = optionalStore.get();
	    
	    if (bindingResult.hasErrors()) {
	    	model.addAttribute("Store", store);
	    	model.addAttribute("reservationInputForm", reservationInputForm);
	    	
	    	return "reservations/register";
	    }
	    
	    reservationService.createReservation(reservationInputForm, store, user);
	    redirectAttributes.addFlashAttribute("successMessage", "予約が完了しました");
	    
	    return "redirect:/reservations";
	}
	
	@PostMapping("/reservations/{resercationId}/delete")
	public String delete(@PathVariable(name = "reservationId") Integer reservationId,
						@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						RedirectAttributes redirectAttributes)
	{
		User user = userDetailsImpl.getUser();
		
		if (user.getRole().getName().equals("ROLE_GENERAL")) {
			redirectAttributes.addFlashAttribute("subscriptionMessage", "この操作を利用するには有料プランへの登録が必要です");
			
			return "redirect:/subscription/register";
		}
		
		Optional<Reservation> optionalReservation = reservationService.findReservationById(reservationId);
		
		if (optionalReservation.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "予約が存在しません");
			
			return "redirect:/reservations";
		}
		
		Reservation reservation = optionalReservation.get();
		
		if (!reservation.getUser().getId().equals(user.getId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです");
			
			return "redirect:/reservations";
		}
		
		reservationService.deleteReservation(reservation);
		redirectAttributes.addFlashAttribute("successMessage", "予約をキャンセルしました。");
		
		return "redirect:/reservations";
	}
}
