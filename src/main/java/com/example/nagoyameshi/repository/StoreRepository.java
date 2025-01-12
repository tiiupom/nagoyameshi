package com.example.nagoyameshi.repository;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
	public Page<Store> findByNameLike(String keyword, Pageable pageable);
	// idカラムの値で降順に並べ替え最初の１件を取得するフォーム
	public Store findFirstByOrderByIdDesc();
	// 店舗名または住所で検索するフォーム
	public Page<Store> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);
	// カテゴリで検索するフォーム
	public Page<Store> findByCategoryLike(String category, Pageable pageable);
}
