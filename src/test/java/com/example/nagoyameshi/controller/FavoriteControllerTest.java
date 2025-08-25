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

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.service.FavoriteService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FavoriteControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private FavoriteService favoriteService;
	
	// お気に入り一覧ページ
	@Test
	public void 未ログインの場合はお気に入り一覧ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/favorites"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 無料会員としてログイン済みの場合はお気に入り一覧ページから有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(get("/favorites"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合はお気に入り一覧ページが正しく表示() throws Exception {
		mockMvc.perform(get("/favorites"))
				.andExpect(status().isOk())
				.andExpect(view().name("favorites/index"));
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合はお気に入り一覧ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/favorites"))
				.andExpect(status().isForbidden());
	}
	
	// お気に入り追加機能
	@Test
	@Transactional
	public void 未ログインの場合はお気に入り追加せずログインページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = favoriteService.countFavorites();
		
		mockMvc.perform(post("/stores/1/favorites/create").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		// テスト後のレコード数を取得
		long countAfter = favoriteService.countFavorites();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合はお気に入り追加せず有料プラン登録ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = favoriteService.countFavorites();
				
		mockMvc.perform(post("/stores/1/favorites/create").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
				
		// テスト後のレコード数を取得
		long countAfter = favoriteService.countFavorites();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合はお気に入り追加後に店舗詳細ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = favoriteService.countFavorites();
				
		mockMvc.perform(post("/stores/1/favorites/create").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("stores/1"));
				
		// テスト後のレコード数を取得
		long countAfter = favoriteService.countFavorites();
		
		// レコード数が変わっていることをチェック
		assertThat(countAfter).isEqualTo(countBefore + 1);
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はお気に入り追加せず403エラー() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = favoriteService.countFavorites();
				
		mockMvc.perform(post("/stores/1/favorites/create").with(csrf()))
				.andExpect(status().isForbidden());
				
		// テスト後のレコード数を取得
		long countAfter = favoriteService.countFavorites();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	// お気に入り解除機能
	@Test
	@Transactional
	public void 未ログインの場合はお気に入り解除せずログインページにリダイレクト() throws Exception {
		mockMvc.perform(post("/favorites/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		Optional<Favorite> optionalFavorite = favoriteService.findFavoriteById(1);
		assertThat(optionalFavorite).isPresent();
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合はお気に入り解除せず有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/favorites/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
		
		Optional<Favorite> optionalFavorite = favoriteService.findFavoriteById(1);
		assertThat(optionalFavorite).isPresent();
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は自身のお気に入り解除後にお気に入り一覧ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/favorites/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/favorites"));

		Optional<Favorite> optionalFavorite = favoriteService.findFavoriteById(1);
		assertThat(optionalFavorite).isEmpty();
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は他人のお気に入り解除せずお気に入り一覧ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/favorites/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/favorites"));

		Optional<Favorite> optionalFavorite = favoriteService.findFavoriteById(1);
		assertThat(optionalFavorite).isPresent();
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はお気に入り解除せず403エラー() throws Exception {
		mockMvc.perform(post("/favorites/1/delete").with(csrf()))
				.andExpect(status().isForbidden());

		Optional<Favorite> optionalFavorite = favoriteService.findFavoriteById(1);
		assertThat(optionalFavorite).isPresent();
	}
}
