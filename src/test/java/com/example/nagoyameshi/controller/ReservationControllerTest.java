package com.example.nagoyameshi.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.service.ReservationService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReservationControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ReservationService reservationService;
	
	// 予約一覧ページ
	@Test
	public void 未ログインの場合は予約一覧ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/reservations"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 無料会員としてログイン済みの場合は予約一覧ページから有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(get("/reservations"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合は予約一覧ページが正しく表示() throws Exception {
		mockMvc.perform(get("/reservations"))
				.andExpect(status().isOk())
				.andExpect(view().name("reservations/index"));
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は予約一覧ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/reservations"))
				.andExpect(status().isForbidden());
	}
	
	// 予約ページ
	@Test
	public void 未ログインの場合は予約ページからログインページにリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/1/reservations/register"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	public void 無料会員としてログイン済みの場合は予約ページから有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(get("/stores/1/reservations/register"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	public void 有料会員としてログイン済みの場合は予約ページが正しく表示() throws Exception {
		mockMvc.perform(get("/stores/1/reservations/register"))
				.andExpect(status().isOk())
				.andExpect(view().name("reservations/register"));
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	public void 管理者としてログイン済みの場合は予約ページが表示されず403エラー() throws Exception {
		mockMvc.perform(get("/stores/1/reservations/register"))
				.andExpect(status().isForbidden());
	}
	
	// 予約機能
	@Test
	@Transactional
	public void 未ログインの場合は予約せずログインページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reservationService.countReservations();
		
		mockMvc.perform(post("/stores/1/reservations/create").with(csrf())
				.param("reservationDate", "2050-01-01")
				.param("reservationTime", "00:00:00")
				.param("numberOfPeople", "10"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
		
		// テスト後のレコード数を取得
		long countAfter = reservationService.countReservations();
		
		// レコード数が変わっていないことを確認
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合は予約せず有料プラン登録ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reservationService.countReservations();
		
		mockMvc.perform(post("/stores/1/reservations/create").with(csrf())
				.param("reservationDate", "2050-01-01")
				.param("reservationTime", "00:00:00")
				.param("numberOfPeople", "10"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/subscription/register"));
		
		// テスト後のレコード数を取得
		long countAfter = reservationService.countReservations();
		
		// レコード数が変わっていないことを確認
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は予約後予約一覧ページにリダイレクト() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reservationService.countReservations();
		
		mockMvc.perform(post("/stores/1/reservations/create").with(csrf())
				.param("reservationDate", "2050-01-01")
				.param("reservationTime", "00:00:00")
				.param("numberOfPeople", "10"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("reservations"));
		
		// テスト後のレコード数を取得
		long countAfter = reservationService.countReservations();
		
		// レコード数が1つ増加していることを検証
		assertThat(countAfter).isEqualTo(countBefore + 1);
		
		Reservation reservation = reservationService.findFirstReservationByOrderByIdDesc();
		assertThat(reservation.getReservedDatetime()).isEqualTo(LocalDateTime.parse("2050-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		assertThat(reservation.getNumberOfPeople()).isEqualTo(10);
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合は予約せず403エラー() throws Exception {
		// テスト前のレコード数を取得
		long countBefore = reservationService.countReservations();
		
		mockMvc.perform(post("/stores/1/reservations/create").with(csrf())
				.param("reservationDate", "2050-01-01")
				.param("reservationTime", "00:00:00")
				.param("numberOfPeople", "10"))
			.andExpect(status().isForbidden());
		
		// テスト後のレコード数を取得
		long countAfter = reservationService.countReservations();
		
		// レコード数が変わっていないことを確認
		assertThat(countAfter).isEqualTo(countBefore);
	}
	
	// 予約キャンセル機能
	@Test
	@Transactional
	public void 未ログインの場合は予約をキャンセルせずログインページにリダイレクト() throws Exception {
		mockMvc.perform(post("/reservations/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
		
		Optional<Reservation> optionalReservation = reservationService.findReservationById(1);
		assertThat(optionalReservation).isPresent();
	}
	
	@Test
	@WithUserDetails("taro.tanaka@example.com")
	@Transactional
	public void 無料会員としてログイン済みの場合は予約をキャンセルせず有料プラン登録ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/reservations/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/subscription/register"));

		Optional<Reservation> optionalReservation = reservationService.findReservationById(1);
		assertThat(optionalReservation).isPresent();		
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は自身の予約キャンセル後に予約一覧ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/reservations/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/reservations"));

		Optional<Reservation> optionalReservation = reservationService.findReservationById(1);
		assertThat(optionalReservation).isPresent();		
	}
	
	@Test
	@WithUserDetails("jiro.suzuki@example.com")
	@Transactional
	public void 有料会員としてログイン済みの場合は他人の予約をキャンセルせず予約一覧ページにリダイレクト() throws Exception {
		mockMvc.perform(post("/reservations/1/delete").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/reservations"));

		Optional<Reservation> optionalReservation = reservationService.findReservationById(1);
		assertThat(optionalReservation).isPresent();		
	}
	
	@Test
	@WithUserDetails("saburo.sato@example.com")
	@Transactional
	public void 管理者としてログイン済みの場合は予約をキャンセルせず403エラー() throws Exception {
		mockMvc.perform(post("/reservations/1/delete").with(csrf()))
				.andExpect(status().isForbidden());

	Optional<Reservation> optionalReservation = reservationService.findReservationById(1);
	assertThat(optionalReservation).isPresent();		
	}
}