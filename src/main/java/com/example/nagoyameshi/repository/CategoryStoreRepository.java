package com.example.nagoyameshi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.CategoryStore;
import com.example.nagoyameshi.entity.Store;

public interface CategoryStoreRepository  extends JpaRepository<CategoryStore, Integer>{
	// 指定された店舗のカテゴリのidをCategoryStoreエンティティのidが小さい順に並べ替えられた状態のリスト形式で取得
	@Query("SELECT cs.category.id FROM categoryStore cs WHERE cs.store = :store ORDER BY cs.id ASC")
	public List<Integer> findCategoryIdsByStoreOrderByIdAsc(@Param("store") Store store);
	
	// 指定した店舗とカテゴリが紐づいたCategoryStoreエンティティを取得
	public Optional<CategoryStore> findByCategoryAndStore(Category category, Store store);
	
	// 指定した店舗に紐づくCategoryStoreエンティティを、idが小さい順に並べ替えられた状態のリスト形式で取得
	public List<CategoryStore> findByStoreOrderByIdAsc(Store store);
}
