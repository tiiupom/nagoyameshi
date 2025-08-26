package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	// 指定した店舗とユーザーが紐づいたお気に入りを取得
	public Favorite findByStoreAndUser(Store store, User user);
	
	// 指定したユーザーのすべてのお気に入りを作成日時が新しい順に並べ替え、ページングされた状態で取得
	Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
