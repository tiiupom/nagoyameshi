package com.example.nagoyameshi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Holiday;
import com.example.nagoyameshi.entity.HolidayStore;
import com.example.nagoyameshi.entity.Store;

public interface HolidayStoreRepository extends JpaRepository<HolidayStore, Integer> {
	/* キーワードで定義できないため、@Queryアノテーションを使って定義
	   指定した店舗のid(Holidaysエンティティのid）をリスト形式で取得するメソッド */
	@Query("SELECT hs.holiday.id FROM HolidayStore hs WHERE hs.store = :store")
	public List<Integer> findHolidayIdsByStore(@Param("store") Store store);
	
	// 指定した店舗と定休日が紐づいたHolidayStoreエンティティを取得するメソッド
	public Optional<HolidayStore> findByHolidayAndStore(Holiday holiday, Store store);
	
	// 指定した店舗に紐づくHolidayStoreエンティティをリスト形式で取得するメソッド
	public List<HolidayStore> findByStore(Store store);
}
