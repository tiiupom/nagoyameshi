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
public class AdminUserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void 未ログインの場合は管理者用の会員一覧ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/users"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("goro.inoue@example.com")
	public void 会員としてログイン済の場合は管理者用の会員一覧ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/admin/users"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("siro.kato@example.com")
	public void 管理者としてログイン済の場合は管理者用の会員一覧ページを表示() throws Exception {
		mockMvc.perform(get("/admin/users"))
				.andExpect(status().isOk())
				.andExpect(view().name("amin/users/index"));
	}
	
	@Test
	public void 未ログインの場合は管理者用の会員詳細ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/users/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("goro.inoue@example.com")
	public void 会員としてログイン済みの場合は管理者用の会員詳細ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/admin/users/1"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("siro.kato@example.com")
	public void 管理者としてログイン済の場合は管理者用の会員詳細ページを表示() throws Exception {
		mockMvc.perform(get("/admin/users/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("amin/users/show"));
	}
}