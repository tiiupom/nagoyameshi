package com.example.nagoyameshi.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	
	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}
	
	// 指定したidを持つ予約を取得
	public Optional<Reservation> findReservationById(Integer id) {
		return reservationRepository.findById(id);
	}
	
	// 指定されたユーザーに紐づく予約を予約日時が新しい順（未来→過去）に並べ替え、ページングされた状態で取得
	public Page<Reservation> findReservationsByUserOrderByReservedDatetimeDesc(User user, Pageable pageable) {
		return reservationRepository.findByUserOrderByReservedDatetimeDesc(user, pageable);
	}
	
	// 予約のレコード数を取得
	public long countReservations() {
		return reservationRepository.count();
	}
	
	// idが最も大きい予約を取得
	public Reservation findFirstReservationByOrderByIdDesc() {
		return reservationRepository.findFirstByOrderByIdDesc();
	}
	
	// フォームから送信された予約情報をデータベースに登録
	@Transactional
	public void createReservation(ReservationRegisterForm reservationRegisterForm, Store store, User user) {
		Reservation reservation = new Reservation();
		LocalDateTime reservedDatetime = LocalDateTime.of(reservationRegisterForm.getReservationDate(), reservationRegisterForm.getReservationTime());
		
		reservation.setReservedDatetime(reservedDatetime);
		reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
		reservation.setStore(store);
		reservation.setUser(user);
		
		reservationRepository.save(reservation);
	}
	
	// 指定した予約をデータベースから削除
	@Transactional
	public void deleteReservation(Reservation reservation) {
		reservationRepository.delete(reservation);
	}
	
	// 予約日時が現在より2時間以上後であればtrueを返す
	/* Durationクラスで時刻の差分を求める。
	 * betweenメソッドは引数に渡した2つの日時の差分を求める
	 * toHoursメソッドで時間の差分を求める	 */
	public boolean isAtLeastTwoHoursInFuture(LocalDateTime reservationDateTime) {
		return Duration.between(LocalDateTime.now(), reservationDateTime).toHours() >= 2;
	}
	
}
