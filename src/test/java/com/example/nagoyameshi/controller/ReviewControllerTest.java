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

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.service.ReviewService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReviewControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ReviewService reviewService;
	
	// レビュー一覧ページ
	@Test
	public void 未ログインの場合はレビュー一覧ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/1/reviews"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 無料会員としてログイン済みの場合はレビュー一覧ページが正しく表示() throws Exception {
		mockMvc.perform(get("/stores/1/reviews"))
				.andExpect(status().isOk())
				.andExpect(view().name("reviews/index"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合はレビュー一覧ページが正しく表示() throws Exception {
		mockMvc.perform(get("/stores/1/reviews"))
				.andExpect(status().isOk())
				.andExpect(view().name("reviews/index"));
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合はレビュー一覧ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/stores/1/reviews"))
				.andExpect(status().isForbidden());
	}
	
	// レビュー投稿ページ
	@Test
	public void 未ログインの場合はレビュー投稿ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/1/reviews/register"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 無料会員としてログイン済みの場合はレビュー投稿ページから有料プラン登録ページんいリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/1/reviews/register"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合はレビュー投稿ページが正しく表示() throws Exception {
		mockMvc.perform(get("/stores/1/reviews/register"))
				.andExpect(status().isOk())
				.andExpect(view().name("reviews/register"));
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合はレビュー投稿ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/stores/1/reviews/register"))
				.andExpect(status().isForbidden());
	}
	
	// レビュー投稿機能
	@Test
	@Transactional
	public void 未ログインの場合はレビューを登録せずにログインページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reviewService.countReviews();
		
		mockMvc.perform(post("/stores/1/reviews/create").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		// テスト後のレコード数を取得
		long countAfter = reviewService.countReviews();
		
		// レコード数が変わっていないことを確認
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合はレビューを登録せずに有料プラン登録ページんいリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reviewService.countReviews();
		
		mockMvc.perform(post("/stores/1/reviews/create").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
		
		// テスト後のレコード数を取得
		long countAfter = reviewService.countReviews();
		
		// レコード数が変わっていないことを確認
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合はレビュー投稿後に店舗詳細ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reviewService.countReviews();
		
		mockMvc.perform(post("/stores/1/reviews/create").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/stores/1"));
		
		// テスト後のレコード数を取得
		long countAfter = reviewService.countReviews();
		
		// レコード数が1つ増加していることを検証
		assertThat(countAfter).isEqualTo(countBefore + 1);
		
		Review review = reviewService.findFirstReviewByOrderByIdDesc();
		assertThat(review.getScore()).isEqualTo(5);
		assertThat(review.getContent()).isEqualTo("テスト感想");
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はレビューを投稿せずに403エラー() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reviewService.countReviews();
		
		mockMvc.perform(post("/stores/1/reviews/create").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().isForbidden());
		
		// テスト後のレコード数を取得
		long countAfter = reviewService.countReviews();
		
		// レコード数が変わっていないことを確認
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	// レビュー編集ページ
	@Test
	public void 未ログインの場合はレビュー編集ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/2/reviews/1/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 無料会員としてログイン済みの場合はレビュー編集ページから有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/2/reviews/1/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("subscription/register"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合は自身のレビュー編集ページが正しく表示() throws Exception {
		mockMvc.perform(get("/stores/2/reviews/1/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("reviews/edit"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合は他人のレビュー編集ページから店舗詳細ページにリダイレクト() throws Exception {
		mockMvc.perform(get("stores/2/reviews/1/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/stores/2"));
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合はレビュー編集ページが表示されずに403エラー() throws Exception {
		mockMvc.perform(get("stores/2/reviews/1/edit"))
				.andExpect(status().isForbidden());
	}
	
	// レビュー更新機能
	@Test
	@Transactional
	public void 未ログインの場合はレビューを更新せずにログインページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/update").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
		Review review = optionalReview.get();
		assertThat(review.getScore()).isEqualTo(3);
		assertThat(review.getContent()).isEqualTo("名古屋では有名な和食のお店。コース料理が充実しています。");
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合はレビューを更新せずに有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/update").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
		Review review = optionalReview.get();
		assertThat(review.getScore()).isEqualTo(3);
		assertThat(review.getContent()).isEqualTo("名古屋では有名な和食のお店。コース料理が充実しています。");
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は自身のレビュー更新後に店舗詳細ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/update").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/stores/2"));
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
		Review review = optionalReview.get();
		assertThat(review.getScore()).isEqualTo(3);
		assertThat(review.getContent()).isEqualTo("名古屋では有名な和食のお店。コース料理が充実しています。");
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は他人のレビューを更新せずに店舗詳細ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/update").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/stores/2"));
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
		Review review = optionalReview.get();
		assertThat(review.getScore()).isEqualTo(3);
		assertThat(review.getContent()).isEqualTo("名古屋では有名な和食のお店。コース料理が充実しています。");
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はレビューを更新せずに403エラー() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/update").with(csrf()).param("score", "5").param("content", "テスト感想"))
				.andExpect(status().isForbidden());
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
		Review review = optionalReview.get();
		assertThat(review.getScore()).isEqualTo(3);
		assertThat(review.getContent()).isEqualTo("名古屋では有名な和食のお店。コース料理が充実しています。");
	}
	
	// レビュー削除機能
	@Test
	@Transactional
	public void 未ログインの場合はレビューを削除せずにログインページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合はレビューを削除せずに有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));

		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合は自身のレビュー削除後に店舗詳細ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/stores/2"));
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合は他人のレビューを削除せずに店舗詳細ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/stores/2"));

		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合はレビューを削除せずに403エラー() throws Exception {
		mockMvc.perform(post("/stores/2/reviews/1/delete").with(csrf()))
				.andExpect(status().isForbidden());
		
		Optional<Review> optionalReview = reviewService.findReviewById(1);
		assertThat(optionalReview).isPresent();
	}
}
