package com.example.nagoyameshi.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.ReservationRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	
	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}
	
	// 指定したidを持つ予約を取得する
	public Optional<Reservation> findReservationById(Integer id) {
		return reservationRepository.findById(id);
	}
	
	// 予約のレコード数を取得
	public long countReservations() {
		return reservationRepository.count();
	}
	
	// idが最も大きい予約を取得
	public Reservation findFirstReservationByOrderByIdDesc() {
		return reservationRepository.findFirstByOrderByIdDesc();
	}
	
	// 指定されたユーザに紐づく予約を作成日時が新しい順に並び替えページングされた状態で取得
	public Page<Reservation> findReservationsByUserOrderByCreatedAtDesc(User user, Pageable pageable) {
		return reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
	}
	
	// 予約人数が定員以下がどうかをチェック
	public boolean isEithinCapacity(Integer numberOfPeople, Integer capacity) {
		return numberOfPeople <= capacity;
	}
	
	// 予約情報を保持する
	@Transactional
	public void createReservation(ReservationInputForm reservationInputForm, Store store, User user) {
	    Reservation reservation = new Reservation();
	    LocalDateTime reservedDatetime = LocalDateTime.of(reservationInputForm.getReservationDate(), reservationInputForm.getReservationTime());

	    reservation.setReservedDatetime(reservedDatetime);
	    reservation.setNumberOfPeople(reservationInputForm.getNumberOfPeople());
	    reservation.setStore(store);
	    reservation.setUser(user);

	    reservationRepository.save(reservation);
	}
	
	// 予約を削除
	@Transactional
	public void deleteReservation(Reservation reservation) {
		reservationRepository.delete(reservation);
	}
	
	// 予約日時が現在よりも30分以上後であればtrueを返す
	public boolean isAtLeastTwoHoursInFuture(LocalDateTime reservationDateTime) {
		return Duration.between(LocalDateTime.now(), reservationDateTime).toHours() >= 0.5;
	}
}
