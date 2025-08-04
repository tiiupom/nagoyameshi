package com.example.nagoyameshi.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.service.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminCategoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CategoryService categoryService;
	
	// 一般ユーザー：taro.samurai@example.com
	// 管理者：hanako.samurai@example.com

	// 管理者用のカテゴリ一覧ページ
	@Test
	public void 未ログインの場合は管理者用のカテゴリ一覧ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/categories"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 一般ユーザーとしてログイン済みの場合は管理者用のカテゴリ一覧ページが表示されず403エラーが発生() throws Exception {
		mockMvc.perform(get("/admin/categories"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は管理者用のカテゴリ一覧ページが正しく表示() throws Exception {
		mockMvc.perform(get("/admin/categories"))
				.andExpect(status().isOk())
				.andExpect(view().name("/admin/categories/index"));
	}
	
	// カテゴリ登録機能
	@Test
	@Transactional
	public void 未ログインの場合はカテゴリを登録せずにログインページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = categoryService.countCategories();
		
		mockMvc.perform(post("/admin/categories/create")
				.with(csrf())
				.param("name", "テストカテゴリ名"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
				
		// テスト後のレコード数を取得
		long countAfter = categoryService.countCategories();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 管理者以外としてログイン済みの場合はカテゴリを登録せずに403エラーが発生() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = categoryService.countCategories();
				
			mockMvc.perform(post("/admin/categories/create")
					.with(csrf())
					.param("name", "テストカテゴリ名"))
					.andExpect(status().isForbidden());
					
		// テスト後のレコード数を取得
		long countAfter = categoryService.countCategories();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はカテゴリ登録後にカテゴリ一覧ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = categoryService.countCategories();
		
		mockMvc.perform(post("/admin/categories/create")
				.with(csrf())
				.param("name", "テストカテゴリ名"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/categories"));
		
		// テスト後のレコード数を取得
		long countAfter = categoryService.countCategories();
		
		// レコード数が1つ増えていることを検証
		assertThat(countAfter).isEqualTo(countBefore +1);
		
		Category category = categoryService.findFirstCategoryByOrderByIdDesc();
		assertThat(category.getName()).isEqualTo("テストカテゴリ名");
	}
	
	// カテゴリ更新機能
	@Test
	public void 未ログインの場合はカテゴリ更新をせずにログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/categories/1/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 管理者以外としてログイン済みの場合はカテゴリ更新をせずに403エラーが発生() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = categoryService.countCategories();
				
			mockMvc.perform(post("/admin/categories/edit")
					.with(csrf())
					.param("name", "テストカテゴリ名"))
					.andExpect(status().isForbidden());
					
		// テスト後のレコード数を取得
		long countAfter = categoryService.countCategories();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はカテゴリ更新後にカテゴリ一覧ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = categoryService.countCategories();
		
		mockMvc.perform(post("/admin/categories/edit")
				.with(csrf())
				.param("name", "テストカテゴリ名"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/categories"));
		
		// テスト後のレコード数を取得
		long countAfter = categoryService.countCategories();
		
		// レコード数が1つ増えていることを検証
		assertThat(countAfter).isEqualTo(countBefore +1);
		
		Category category = categoryService.findFirstCategoryByOrderByIdDesc();
		assertThat(category.getName()).isEqualTo("テストカテゴリ名");
	}
	
	// カテゴリ削除機能
	@Test
	public void 未ログインの場合はカテゴリを削除せずにログインページにリダイレクト() throws Exception {
		mockMvc.perform(post("/admin/categories/1/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		Optional<Category> optionalCategory = categoryService.findCategoryById(1);
		assertThat(optionalCategory).isPresent();
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 管理者以外としてログイン済みの場合はカテゴリを削除せずに403エラーが発生() throws Exception {
		mockMvc.perform(post("/admin/categories/1/delete"))
				.andExpect(status().isForbidden());

		Optional<Category> optionalCategory = categoryService.findCategoryById(1);
		assertThat(optionalCategory).isPresent();
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はカテゴリ削除後にカテゴリ一覧ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/admin/categories/1/delete"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/admin/categories"));

		Optional<Category> optionalCategory = categoryService.findCategoryById(1);
		assertThat(optionalCategory).isEmpty();
	}
	
}
