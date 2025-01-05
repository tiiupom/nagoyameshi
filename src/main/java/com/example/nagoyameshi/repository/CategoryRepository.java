package com.example.nagoyameshi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	// 指定した店舗とカテゴリが紐づいたcategoryエンティティを取得
	public Optional<Category> findByCategoryAndStore(Category category, Store store);
	
	// 指定した店舗に紐づくcategoryエンティティをidが小さい順に並べ替えた状態のリスト形式で取得
	public List<Category> findByStoreOederByIdAsc(Store store);
}
