package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.CategoryStore;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.CategoryStoreRepository;

@Service
public class CategoryStoreService {
	private final CategoryStoreRepository categoryStoreRepository;
	private final CategoryService categoryService;
	
	public CategoryStoreService(CategoryStoreRepository categoryStoreRepository, CategoryService categoryService) {
		this.categoryStoreRepository = categoryStoreRepository;
		this.categoryService = categoryService;
	}
	
	// 指定した店舗のカテゴリのidをリスト形式で取得
	public List<Integer> findCategoryIdsByStoreOrderByIdAsc(Store store) {
		return categoryStoreRepository.findCategoryIdsByStoreOrderByIdAsc(store);
	}
	
	// フォームから送信されたカテゴリのidリストをもとにデータベースに登録
	@Transactional
	public void createCategoryStores(List<Integer> categoryIds, Store store) {
		for (Integer categoryId : categoryIds) {
			if (categoryId != null) {
				Optional<Category> optionalCategory = categoryService.findCategoryById(categoryId);
				
				if (optionalCategory.isPresent()) {
					Category category = optionalCategory.get();
					
					Optional<CategoryStore> optionalCurrentCategoryStore = categoryStoreRepository.findByCategoryAndStore(category, store);
					
					// 重複するエンティティが存在しない場合は新たにエンティティを作成
					if (optionalCurrentCategoryStore.isEmpty()) {
						CategoryStore categoryStore = new CategoryStore();
						categoryStore.setStore(store);
						categoryStore.setCategory(category);
						
						categoryStoreRepository.save(categoryStore);
					}
				}
			}
		}
	}
	
	// フォームから送信されたカテゴリのidリストをもとにデータベースと同期
	@Transactional
	public void syncCategoryStores(List<Integer> newCategoryIds, Store store) {
		List<CategoryStore> currentCategoryStores = categoryStoreRepository.findByStoreOrderByIdAsc(store);
		
		if (newCategoryIds == null) {
			// newCategoryIdsがnullの場合はすべてのエンティティを削除
			for (CategoryStore currentCategoryStore : currentCategoryStores) {
				categoryStoreRepository.delete(currentCategoryStore);
			}
		} else {
			// 既存のエンティティが新しいリストに存在しない場合は削除
			for (CategoryStore currentCategoryStore : currentCategoryStores) {
				if (!newCategoryIds.contains(currentCategoryStore.getCategory().getId())) {
					categoryStoreRepository.delete(currentCategoryStore);
				}
			}
			
			for (Integer newCategoryId : newCategoryIds) {
				if (newCategoryId != null) {
					Optional<Category> optionalCategory = categoryService.findCategoryById(newCategoryId);
					
					if (optionalCategory.isPresent()) {
						Category category = optionalCategory.get();
						
						Optional<CategoryStore> optionalCurrentCategoryStore = categoryStoreRepository.findByCategoryAndStore(category, store);
						
						// 重複するエンティティが存在しない場合は新たにエンティティを作成
						if (optionalCurrentCategoryStore.isEmpty()) {
							CategoryStore categoryStore = new CategoryStore();
							categoryStore.setStore(store);
							categoryStore.setCategory(category);
							
							categoryStoreRepository.save(categoryStore);
						}
					}
				}
			}
		}
	}
}
