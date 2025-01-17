package com.example.nagoyameshi.controller;
  
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.StoreEditForm;
import com.example.nagoyameshi.form.StoreRegisterForm;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.StoreService;

/* @RequestParam リクエストパラメータの値を引数にバインドする
 * 		name 取得するリクエストパラメータ名
 * 		required リクエストパラメータが必須か
 * 		dafultValue リクエストパラメータが未指定または空の場合のデフォルト値
 * if文　keywordパラメータが存在する場合は部分一致検索を行い、そうでなければ前件のデータを取得
 */ 
@Controller
@RequestMapping("/admin/stores")
public class AdminStoreController {
	private final StoreService storeService;
	private final CategoryService categoryService;
	
	public AdminStoreController(StoreService storeService, CategoryService categoryService) {
		this.storeService = storeService;
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
						Model model)
	{
		//System.out.println("aaa");
		
		Page<Store> storePage;
		
		if (keyword != null && !keyword.isEmpty()) {
			storePage = storeService.findStoresByNameLike(keyword, pageable);
		} else {
			storePage = storeService.findAllStores(pageable);
		}
		
		model.addAttribute("storePage", storePage);
		model.addAttribute("keyword", keyword);	// ビューにkeyword（文字列）を渡す
		
		return "admin/stores/index";
	}
	
	/* @PathVariable URLの一部を引数にバインドでき、変数のように扱いコントローラ内で値を利用できる
	 * if文 OptionalクラスのisEmpty()を使い中身が空であればtrue、空でなければfalseを返す
	 * Optional<Store>型のままではエンティティの各フィールドにアクセスできないため、optionalクラスのget()でStore型に変換しビューに渡す */
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		Optional<Store> optionalStore = storeService.findStoreById(id);
		//System.out.println(optionalStore); ok
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません");
			
			return "redirect:/admin/stores";
		}
		
		Store store = optionalStore.get();
		model.addAttribute("store", store);
		//System.out.println(store); ok
		return "admin/stores/show";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		List<Category> categories = categoryService.findAllCategories();
		model.addAttribute("storeRegisterForm", new StoreRegisterForm());
		model.addAttribute("categories", categories);
		
		return "admin/stores/register";
	}
	
	/* if文　フォーム入力内容に対しバリデーションを行いエラーであれば管理者用の店舗登録ページを再度表示する
	 * 		エラーがなければサービスクラスに定義したcreateStore()を実行し店舗データをstoresテーブルに保存
	 * 		追加後、フラッシュメッセージを渡し管理者用の店舗一覧ページにリダイレクト */
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated StoreRegisterForm storeRegisterForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (bindingResult.hasErrors()) {
			List<Category> categories = categoryService.findAllCategories();
			model.addAttribute("storeRegisterForm", storeRegisterForm);
			model.addAttribute("categories", categories);
			
			return "admin/stores/register";
		}
		
		storeService.createStore(storeRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました");
		
		return "redirect:/admin/stores";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		Optional<Store> optionalStore = storeService.findStoreById(id);
		//System.out.println(optionalStore); ok
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません");
			
			return "redirect:/admin/stores";
		}
		
		Store store = optionalStore.get();
		StoreEditForm storeEditForm = new StoreEditForm(store.getName(), null, store.getCategory(), store.getDescription(), store.getStartTime(), store.getEndTime(), store.getPriceMin(), store.getPriceMax(), store.getAddress(), store.getPhoneNumber(), store.getCapacity());
		
		model.addAttribute("store", store);
		model.addAttribute("storeEditForm", storeEditForm);
		//System.out.println(store); ok
		return "admin/stores/edit";
	}
	
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated StoreEditForm storeEditForm,
						 BindingResult bindingResult,
						 @PathVariable(name = "id") Integer id,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		Optional<Store> optionalStore = storeService.findStoreById(id);
		
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません");
			
			return "redirect:/admin/stores";
		}
		
		Store store = optionalStore.get();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			model.addAttribute("storeEditForm", storeEditForm);
			
			return "admin/stores/edit";
		}
		
		storeService.updateStore(storeEditForm, store);
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報の編集内容を保存しました");
		
		return "redirect:/admin/stores";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		Optional<Store> optionalStore =storeService.findStoreById(id);
		
		if (optionalStore.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません");
			
			return "redirect:/admin/stores";
		}
		
		Store store = optionalStore.get();
		storeService.deleteStore(store);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました");
		
		return "redirect:/admin/stores";
	}
}
