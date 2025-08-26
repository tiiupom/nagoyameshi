package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	
	// 指定した店舗とユーザーが紐づいたレビューを取得
	public Review findByStoreAndUser(Store store, User user);
	
	// 指定した店舗のすべてのレビューを作成日時が新しい順に並べ替え、ページングされた状態で取得
	public Page<Review> findByStoreOrderByCreatedAtDesc(Store store, Pageable pageable);
	
	// idが最も大きいレビューを取得（idを基準に降順で並べ替え、最初の1件を取得）
	public Review findFirstByOrderByIdDesc(); 
	
}
