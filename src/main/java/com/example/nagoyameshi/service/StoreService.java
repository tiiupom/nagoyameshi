package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.StoreRegisterForm;
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
	
	// 店舗のレコード数を取得（テスト前とテスト後のレコード数を比べ店舗登録の成功を確認する為）
	public long countStores() {
		return storeRepository.count();
	}
	
	// idが最も大きい店舗を取得
	public Store findFirstStoreByOrderByIdDesc() {
		return storeRepository.findFirstByOrderByIdDesc();
	}
	
	/* 送信された画像ファイルをstorageフォルダに保存
	 *  UUIDを使って生成したファイル名を返す
	 * generateNewFileName()
	 * 	ファイル名の重複を防ぐため、ファイル名をUUIDで別名に変更して保存する
	 * copyImageFile()
	 * 	変更後JavaのFileクラスが提供するcopy()で画像ファイルをコピー
	 */
	@Transactional
	public void createStore(StoreRegisterForm storeRegisterForm) {
		Store store = new Store();
		MultipartFile imageFile = storeRegisterForm.getImageFile();
		System.out.println(store);
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageName(hashedImageName);
		}
		
		store.setName(storeRegisterForm.getName());
		store.setDescription(storeRegisterForm.getDescription());
		store.setStartTime(storeRegisterForm.getStartTime());
		store.setEndTime(storeRegisterForm.getEndTime());
		store.setPriceMin(storeRegisterForm.getPriceMin());
		store.setPriceMax(storeRegisterForm.getPriceMax());
		store.setAddress(storeRegisterForm.getAddress());
		store.setPhoneNumber(storeRegisterForm.getPhoneNumber());
		store.setCapacity(storeRegisterForm.getCapacity());
		System.out.println(store);
		storeRepository.save(store);
	}
	
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		
		for (int i = 0; i < fileNames.length -1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		
		String hashedFileName = String.join(".", fileNames);
		
		return hashedFileName;
	}
	
	// 画像ファイルを指定したファイル名にコピー
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
