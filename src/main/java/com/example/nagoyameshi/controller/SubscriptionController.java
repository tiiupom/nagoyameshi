package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;

@Controller
public class SubscriptionController {
	@Value("${stripe.premium-plan-price-id}")
	private String premiumPlanPriceId;
	
	private final UserService userService;
	private final StripeService stripeService;
	
	public SubscriptionController(UserService userService, StripeService stripeService) {
		this.userService = userService;
		this.stripeService = stripeService;
	}
	
	@GetMapping("/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @RequestParam String paymentMethodId, RedirectAttributes redirectAttributes) {
		User user = userDetailsImpl.getUser();
		
		// ユーザのstripeCustomerIdフィールドがNULL（初めてユーザがサブスクリプションに加入する場合の処理
		if (user.getStripeCustomerId() == null) {
			try {
				// 顧客(StripeのCustomerオブジェクト）を作成
				Customer customer = stripeService.createCustomer(user);
				
				// stripecustomerIdフィールドに顧客IDを保存
				userService.saveStripeCustomerId(user, customer.getId());
			} catch (StripeException e) {
				redirectAttributes.addFlashAttribute("errorMessage", "有料プランへの登録に失敗しました。再度お試しください。");
				
				return "redirect:/";
			}
		}
		
		String stripeCustomerId = user.getStripeCustomerId();
		
		try {
			// フォームから送信された支払方法(StripeのPaymentMethodオブジェクト）を顧客に紐づけ
			stripeService.attachPaymentMethodToCustomer(paymentMethodId, stripeCustomerId);
			
			// フォームから送信された支払方法を顧客のデフォルトの支払方法に設定
			stripeService.setDefaultPaymentMethod(paymentMethodId, stripeCustomerId);
			
			// サブスクリプション(StripeのSubscriptionオブジェクト）を作成
			stripeService.createSubscription(stripeCustomerId, premiumPlanPriceId);
	
		} catch (StripeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "有料プランへの登録に失敗しました。再度お試しください、");
			
			return "redirect:/";
		}
		
		// ユーザのロールを更新
		userService.updateRole(user, "ROLE_SUBSCRIBER");
		userService.refreshAuthenticationByRole("ROLE_SUBSCRIBER");
		
		redirectAttributes.addFlashAttribute("successMessage", "有料プランへの登録が完了しました。");
		
		return "redirect:/";
	}
	
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes, Model model) {
		User user = userDetailsImpl.getUser();
		
		try {
			// 顧客のデフォルトの支払方法（StripeのPaymentMethodオブジェクト）を取得
			PaymentMethod paymentMethod = stripeService.getDefaultPaymentMethod(user.getStripeCustomerId());
			
			model.addAttribute("card", paymentMethod.getCard());
			model.addAttribute("cardHolderName", paymentMethod.getBillingDetails().getName());
		} catch (StripeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "お支払方法を取得できませんでした。再度お試しください。");
			
			return "redirect:/";
		}
		
		return "subscription/edit";
	}
	
	@PostMapping("/update")
	public String update(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @RequestParam String paymentMethodId, RedirectAttributes redirectAttributes) {
		User user = userDetailsImpl.getUser();
		String stripeCustomerId = user.getStripeCustomerId();
		
		try {
			//　現在のデフォルトの支払方法（StripeのPaymentMethodオブジェクト）のIDを取得
			String currentDefaultPaymentMethodId = stripeService.getDefaultPaymentMethodId(stripeCustomerId);
			
			// フォームから送信された支払い方法を顧客（StripeのCustomerオブジェクト）に紐づけ
			stripeService.attachPaymentMethodToCustomer(paymentMethodId, stripeCustomerId);
			
			// フォームから送信された支払い方法を顧客のデフォルトの支払い方法に設定
			stripeService.setDefaultPaymentMethod(paymentMethodId, stripeCustomerId);
			
			// 以前のデフォルトの支払い方法と顧客の紐づけを解除
			stripeService.detachPaymentMethodFromCustomer(currentDefaultPaymentMethodId);
			
		} catch (StripeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "お支払い方法の変更に失敗しました。再度お試しください。");
			
			return "redirect:/";
		}
		
		redirectAttributes.addFlashAttribute("successMessage", "お支払い方法を変更しました。");
		
		return "redirect:/";
	}
	
	@GetMapping("/cancel")
	public String cancel() {
		return "subscription/cancel";
	}
	
	@PostMapping("/delete")
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
		User user = userDetailsImpl.getUser();
		
		try {
			// 顧客が契約中のサブスクリプション（StripeのSubscriptionオブジェクト）を取得
			List<Subscription> subscriptions = stripeService.getSubscriptions(user.getStripeCustomerId());
			
			// 顧客が契約中のサブスクリプションをキャンセル
			stripeService.cancelSubscriptions(subscriptions);
			
			// デフォルトの支払い方法（StripeのPaymentMethodオブジェクト）のIDを取得
			String defaultPaymentMethodId = stripeService.getDefaultPaymentMethodId(user.getStripeCustomerId());
			
			// デフォルトの支払い方法と顧客の紐づけを解除
			stripeService.detachPaymentMethodFromCustomer(defaultPaymentMethodId);
		} catch (StripeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "有料プランの解約に失敗しました。再度お試しください。");
			
			return "redirect:/";
		}
		
		// ユーザーのロールを更新
		userService.updateRole(user, "ROLE_SUBSCRIBER");
		userService.refreshAuthenticationByRole("ROLE_SUBSCRIBER");
		
		redirectAttributes.addFlashAttribute("successMessage", "有料プランを解約しました。");
		
		return "redirect:/";
	}
}
