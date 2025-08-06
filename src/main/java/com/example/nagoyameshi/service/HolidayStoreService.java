package com.example.nagoyameshi.service;

import java.util.List;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.HolidayStoreRepository;

public class HolidayStoreService {
	private final HolidayStoreRepository holidayStoreRepository;
	private final HolidayService holidayService;
	
	public HolidayStoreService(HolidayStoreRepository holidayStoreRepository, HolidayService holidayService) {
		this.holidayStoreRepository = holidayStoreRepository;
		this.holidayService = holidayService;
	}
	
	/* 指定した店舗の定休日のid（Holidayエンティティのid）をリスト形式で取得
	 * holidayStoreRepositoryで定義したメソッドを呼び出す	 */
	public List<Integer> findHolidayIdsByStore(Store store) {
		return holidayStoreRepository.findHolidayIdsByStore(store);
	}
	
	// フォームから送信された定休日のidリストをもとにholidays_storeテーブルにデータを登録
	
	
	// フォームから送信された定休日のidリストをもとに、holidays_storeテーブルのデータを同期
	
	
}
