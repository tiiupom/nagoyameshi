package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.StoreRepository;

@Service
public class StoreService {
	private final StoreRepository storeRepository;
	
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	// 全ての民宿をページングされた状態で取得
	public Page<Store> findAllStores(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}
	
	// 指定キーワードを店舗名に含む民宿をページングされた状態で取得
	public Page<Store> findStoresByNameLike(String keyword, Pageable pageable) {
		return storeRepository.findByNameLike("%" + keyword + "%", pageable);
	}
	
	// 指定したidを持つ民宿を取得
	// Optional nullを持つ可能性のある（nullかもしれない）オブジェクトをより安全・便利に扱うためのクラス
	public Optional<Store> findStoreById(Integer id) {
		return storeRepository.findById(id);
	}
}
