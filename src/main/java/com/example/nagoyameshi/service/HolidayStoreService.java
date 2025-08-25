package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Holiday;
import com.example.nagoyameshi.entity.HolidayStore;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.HolidayStoreRepository;

@Service
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
	
	/* フォームから送信された定休日のidリストをもとにholidays_storeテーブルにデータを登録
	 * 拡張for文を使い、定休日のidのリストの要素数だけ繰り返し処理
	 * 拡張for文のブロック内においてその要素がnullでなければ処理を行う
	 * 	１　要素（定休日のid）に一致するRegularHolidayエンティティを取得
	 * 	２　RegularHolidayエンティティが存在すれば、以下の処理を行う
	 * 		１　店舗（引数で受け取ったRestaurantエンティティ）と上記のRegularHolidayエンティティが
	 		　紐づいたRegularHolidayRestaurantエンティティを取得
	 		２　RegularHolidayRestaurantエンティティが存在しなければ、
	 		　新しくRegularHolidayRestaurantエンティティの
	 		　インスタンスを生成し、各フィールドに値をセットしたあと保存する　*/
	@Transactional
	public void createHolidayStore(List<Integer> holidayIds, Store store) {
		for (Integer holidayId : holidayIds) {
			if (holidayId != null) {
				Optional<Holiday> optionalHoliday = holidayService.findHolidayByID(holidayId);
				
				if (optionalHoliday.isPresent()) {
					Holiday holiday = optionalHoliday.get();
					
					Optional<HolidayStore> optionalCurrentHolidayStore = holidayStoreRepository.findByHolidayAndStore(holiday, store);
					
					// 重複するエンティティが存在しない場合は新たにエンティティを作成
					if (optionalCurrentHolidayStore.isEmpty()) {
						HolidayStore holidayStore = new HolidayStore();
	                       holidayStore.setStore(store);
	                       holidayStore.setHoliday(holiday);
	                       
	                       holidayStoreRepository.save(holidayStore);
					}
				}
			}
		}
	}
	
	// フォームから送信された定休日のidリストをもとに、holidays_storeテーブルのデータを同期
	@Transactional
	public void syncHolidayStore(List<Integer> newHolidayIds,Store store) {
		List<HolidayStore> currentHolidayStores = holidayStoreRepository.findByStore(store);
		
		if (newHolidayIds == null) {
			// newHolidayIdsがnullの場合は全てのエンティティを削除
			for (HolidayStore curreHolidayStore : currentHolidayStores) {
				holidayStoreRepository.delete(curreHolidayStore);
			}
		} else {
			// 既存のエンティティが新しいリストに存在しない場合は削除
			for (HolidayStore currentHolidayStore : currentHolidayStores) {
				if (!newHolidayIds.contains(currentHolidayStore.getHoliday().getId())) {
					holidayStoreRepository.delete(currentHolidayStore);
				}
			}
			
			for (Integer newHolidayId : newHolidayIds) {
				if (newHolidayId != null) {
					Optional<Holiday> optionalHoliday = holidayService.findHolidayByID(newHolidayId);
					
					if (optionalHoliday.isPresent()) {
						Holiday holiday = optionalHoliday.get();
						
						Optional<HolidayStore> optionalCurrentHolidayStore = holidayStoreRepository.findByHolidayAndStore(holiday, store);
						
						// 重複しないエンティティが存在しない場合は新たにエンティティを作成
						if (optionalCurrentHolidayStore.isEmpty()) {
							HolidayStore holidayStore = new HolidayStore();
							holidayStore.setStore(store);
							holidayStore.setHoliday(holiday);
							
							holidayStoreRepository.save(holidayStore);
						}
					}
				}
			}
		}
	}
}
