package com.example.nagoyameshi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminStoreControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void 未ログインの場合は管理者用の店舗一覧ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/stores"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 管理者以外でログイン済の場合は管理者用の店舗一覧ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/admin/stores"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済の場合は管理者用の店舗一覧ページが表示() throws Exception {
		mockMvc.perform(get("/admin/stores"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/stores/index"));
	}
	
	@Test
	public void 未ログインの場合は管理者用の店舗詳細ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/stores/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 管理者以外でログイン済みの場合は管理者用の店舗詳細ページが表示されずに403エラー() throws Exception {
		mockMvc.perform(get("/admin/stores/1"))
		 		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は店舗詳細ページを表示() throws Exception {
		mockMvc.perform(get("/admin/stores/1"))
		 		.andExpect(status().isOk())
		 		.andExpect(view().name("admin/stores/show"));
	}
}
