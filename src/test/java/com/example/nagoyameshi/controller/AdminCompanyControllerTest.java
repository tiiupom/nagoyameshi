package com.example.nagoyameshi.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.service.CompanyService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminCompanyControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CompanyService companyService;
	
	// 管理者用の会社概要ページ
	@Test
	public void 未ログインの場合は会社概要ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/company"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 管理者以外でログイン済みの場合は管理者用の会社概ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/admin/company"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は管理者用の会社概要ページが正しく表示() throws Exception {
		mockMvc.perform(get("/admin/company"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/company/index"));
	}
	
	// 管理者用の会社概要編集ページ
	@Test
	public void 未ログインの場合は管理者用の会社概要編集ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/company/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 管理者以外でログイン済みの場合は管理者用の会社概要編集ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/admin/company/edit"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は管理者用の会社概要編集ページが正しく表示() throws Exception {
		mockMvc.perform(get("/admin/company/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/company/edit"));
	}
	
	// 会社概要更新機能
	@Test
	@Transactional
	public void 未ログインの場合は会社概要を更新せずにログインページにリダイレクト() throws Exception {
		mockMvc.perform(post("/admin/company/update")
				.with(csrf())
				.param("name", "テスト会社名")
				.param("address", "テスト住所")
				.param("representative", "テスト代表者名")
				.param("establishmentDate", "2015年3月19日")
				.param("capital", "5,000千万")
				.param("business", "テスト事業内容")
				.param("numberOfEmployees", "35名"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
		
		Company company = companyService.findFirstCompanyByOrderByIdDesc();
		assertThat(company.getName()).isEqualTo("NAGOYAMESHI株式会社");
		assertThat(company.getAddress()).isEqualTo("東京都千代田区神田練堀町300番地　住友不動産秋葉原駅前ビル5F");
		assertThat(company.getRepresentative()).isEqualTo("佐藤　三郎");
		assertThat(company.getEstablishmentDate()).isEqualTo("2000年4月15日");
		assertThat(company.getCapital()).isEqualTo("110,000千円");
		assertThat(company.getBusiness()).isEqualTo("飲食店等の情報提供サービス");
		assertThat(company.getNumberOfEmployees()).isEqualTo("83名");
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 管理者以外でログイン済みの場合は会社概要を更新せずに403エラー() throws Exception {
		mockMvc.perform(post("/admin/company/update")
				.with(csrf())
				.param("name", "テスト会社名")
				.param("address", "テスト住所")
				.param("representative", "テスト代表者名")
				.param("establishmentDate", "2015年3月19日")
				.param("capital", "5,000千万")
				.param("business", "テスト事業内容")
				.param("numberOfEmployees", "35名"))
			.andExpect(status().isForbidden());
		
		Company company = companyService.findFirstCompanyByOrderByIdDesc();
		assertThat(company.getName()).isEqualTo("NAGOYAMESHI株式会社");
		assertThat(company.getAddress()).isEqualTo("東京都千代田区神田練堀町300番地　住友不動産秋葉原駅前ビル5F");
		assertThat(company.getRepresentative()).isEqualTo("佐藤　三郎");
		assertThat(company.getEstablishmentDate()).isEqualTo("2000年4月15日");
		assertThat(company.getCapital()).isEqualTo("110,000千円");
		assertThat(company.getBusiness()).isEqualTo("飲食店等の情報提供サービス");
		assertThat(company.getNumberOfEmployees()).isEqualTo("83名");
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は会社概要更新後に会社概要ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/admin/company/update")
				.with(csrf())
				.param("name", "テスト会社名")
				.param("address", "テスト住所")
				.param("representative", "テスト代表者名")
				.param("establishmentDate", "2015年3月19日")
				.param("capital", "5,000千万")
				.param("business", "テスト事業内容")
				.param("numberOfEmployees", "35名"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/company"));
		
		Company company = companyService.findFirstCompanyByOrderByIdDesc();
		assertThat(company.getName()).isEqualTo("NAGOYAMESHI株式会社");
		assertThat(company.getAddress()).isEqualTo("東京都千代田区神田練堀町300番地　住友不動産秋葉原駅前ビル5F");
		assertThat(company.getRepresentative()).isEqualTo("佐藤　三郎");
		assertThat(company.getEstablishmentDate()).isEqualTo("2000年4月15日");
		assertThat(company.getCapital()).isEqualTo("110,000千円");
		assertThat(company.getBusiness()).isEqualTo("飲食店等の情報提供サービス");
		assertThat(company.getNumberOfEmployees()).isEqualTo("83名");
	}
}

