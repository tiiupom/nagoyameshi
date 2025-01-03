package com.example.nagoyameshi.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.service.StoreService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminStoreControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private StoreService storeService;
	
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
	
	@Test
	public void 未ログインの場合は管理者用の店舗登録ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/admin/stores/register"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 管理者以外でログイン済の場合は店舗登録ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/admin/stores/register"))
		 		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済の場合は管理者用の店舗登録ページが正しく表示される() throws Exception {
		mockMvc.perform(get("/admin/stores/register"))
		  		.andExpect(status().isOk())
		  		.andExpect(view().name("admin/stores/register"));
	}
	
	@Test
	@Transactional
	public void 未ログインの場合は店舗を登録せずログインページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = storeService.countStores();
		
		// テスト後の画像ファイルデータを準備
		Path filePath = Paths.get("src/main/resource/static/storage/store01.jpg");
		String fileName = filePath.getFileName().toString();
		String fileType = Files.probeContentType(filePath);
		byte[] fileBytes = Files.readAllBytes(filePath);
		
		MockMultipartFile imageFile = new MockMultipartFile(
			"imageFile",	// フォームのname属性の値
			fileName,		// ファイル名
			fileType,		// ファイル形式
			fileBytes		// ファイルのバイト配列
		);
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/stores/create").file(imageFile)
				.with(csrf())
				.param("name", "テスト店舗名")
				.param("description", "店舗説明")
				.param("startTime", "09:00")
				.param("endTime", "19:00")
				.param("priceMin", "1000")
				.param("priceMax", "2500")
				.param("address", "テスト住所")
				.param("phoneNumber", "000-000-000")
				.param("holidays", "月")
				.param("capacity", "30"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
		
		// テスト後のレコード数を取得
		long countAfter = storeService.countStores();
		
		// レコード数が変わっていないことを検証
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 一般ユーザーとしてログイン済の場合は店舗登録せずに403エラー表示() throws Exception {
		// テスト前のレコード数を取得
				long countBefore = storeService.countStores();
				
				// テスト後の画像ファイルデータを準備
				Path filePath = Paths.get("src/main/resource/static/storage/store01.jpg");
				String fileName = filePath.getFileName().toString();
				String fileType = Files.probeContentType(filePath);
				byte[] fileBytes = Files.readAllBytes(filePath);
				
				MockMultipartFile imageFile = new MockMultipartFile(
					"imageFile",	// フォームのname属性の値
					fileName,		// ファイル名
					fileType,		// ファイル形式
					fileBytes		// ファイルのバイト配列
				);
				
				mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/stores/create").file(imageFile)
						.with(csrf())
						.param("name", "テスト店舗名")
						.param("description", "テスト説明")
						.param("startTime", "09:00")
						.param("endTime", "19:00")
						.param("priceMin", "1000")
						.param("priceMax", "2500")
						.param("address", "テスト住所")
						.param("phoneNumber", "000-000-000")
						.param("holidays", "月")
						.param("capacity", "30"))
					.andExpect(status().isForbidden());
				
				// テスト後のレコード数を取得
				long countAfter = storeService.countStores();
				
				// レコード数が変わっていないことを検証
				assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済の場合は店舗登録後店舗一覧ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
				long countBefore = storeService.countStores();
				
				// テスト後の画像ファイルデータを準備
				Path filePath = Paths.get("src/main/resource/static/storage/store01.jpg");
				String fileName = filePath.getFileName().toString();
				String fileType = Files.probeContentType(filePath);
				byte[] fileBytes = Files.readAllBytes(filePath);
				
				MockMultipartFile imageFile = new MockMultipartFile(
					"imageFile",	// フォームのname属性の値
					fileName,		// ファイル名
					fileType,		// ファイル形式
					fileBytes		// ファイルのバイト配列
				);
				
				mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/stores/create").file(imageFile)
						.with(csrf())
						.param("name", "テスト店舗名")
						.param("description", "テスト説明")
						.param("startTime", "09:00")
						.param("endTime", "19:00")
						.param("priceMin", "1000")
						.param("priceMax", "2500")
						.param("address", "テスト住所")
						.param("phoneNumber", "000-000-000")
						.param("holidays", "月")
						.param("capacity", "30"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/admin/stores"));
				
				// テスト後のレコード数を取得
				long countAfter = storeService.countStores();
				
				// レコード数が1つ増加していることを検証
				assertThat(countAfter).isEqualTo(countBefore +1);
				
				Store store = storeService.findFirstStoreByOrderByIdDesc();
				assertThat(store.getName()).isEqualTo("テスト店舗");
				assertThat(store.getDescription().isEqualTo("テスト説明");
				assertThat(store.getStartTime()).isequalTo("09:00");
				assertThat(store.getEndTime()).isequalTo("19:00");
				assertThat(store.getPriceMin()).isequalTo(1000);
				assertThat(store.getPriceMax()).isequalTo(2500);
				assertThat(store.getAddress()).isequalTo("テスト住所");
				assertThat(store.getPhoneNumber()).isequalTo("000-000-000");
				assertThat(store.getHolidays()).isequalTo("月");
				assertThat(store.getCapacity()).isequalTo(30);
	}
}
