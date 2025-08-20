package com.example.nagoyameshi.repository;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	// 指定されたユーザーに紐づく予約を予約日時が新しい順（未来→過去）に並べ替え、ページングされた状態で取得
	public Page<Reservation> findByUserOrderByReservedDatetimeDesc(User user, Pageable pageable);
	
	// idが最も大きい予約を取得（idを基準に降順で並べ替え、最初の1件を取得）
	public Reservation findFirstByOrderByIdDesc();
}
