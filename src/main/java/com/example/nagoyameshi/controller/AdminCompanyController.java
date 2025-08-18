package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.form.CompanyEditForm;
import com.example.nagoyameshi.service.CompanyService;

@Controller
@RequestMapping("/admin/company")
public class AdminCompanyController {
	private final CompanyService companyService;
	
	public AdminCompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	// 管理者用の会社概要ページ（admin/company/index.htmlファイル）を表示
	@GetMapping
	public String index(Model model) {
		Company company = companyService.findFirstCompanyByOrderByIdDesc();
		model.addAttribute("company", company);
		
		return "admin/company/index";
	}
	
	// 管理者用の会社概要編集ページ（admin/company/edit.html）を表示
	@GetMapping("/edit")
	public String edit(Model model) {
		Company company = companyService.findFirstCompanyByOrderByIdDesc();
		CompanyEditForm companyEditForm = new CompanyEditForm(company.getName(),
															  company.getAddress(),
															  company.getRepresentative(),
															  company.getEstablishmentDate(),
															  company.getCapital(),
															  company.getBusiness(),
															  company.getNumberOfEmployees());
		model.addAttribute("companyEditForm", companyEditForm);
		
		return "admin/company/edit";
	}
	
	// フォームから送信された会社概要の内容でデータベースを更新
	/* BindingResult型の引数を設定しエラーをチェック
	 * エラーがある場合は管理者用の会社概要編集ページを再度表示
	 * エラーがない場合はサービスクラスに定義したupdatecompany()メソッドでデータベースの会社
	 * 概要を更新し管理者用の会社概要ページにリダイレクト */
	@PostMapping("/update")
	public String update(@ModelAttribute @Validated CompanyEditForm companyEditForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (bindingResult.hasErrors()) {
			model.addAttribute("companyEditForm", companyEditForm);
			
			return "admin/company/edit";
		}
		
		Company company = companyService.findFirstCompanyByOrderByIdDesc();
		companyService.updateCompany(companyEditForm, company);
		redirectAttributes.addFlashAttribute("successMessage", "会社概要を編集しました。");
		
		return "redirect:/admin/company";
	}
	
}
