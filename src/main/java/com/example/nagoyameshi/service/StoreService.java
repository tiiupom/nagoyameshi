package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.StoreEditForm;
import com.example.nagoyameshi.form.StoreRegisterForm;
import com.example.nagoyameshi.repository.StoreRepository;
 
@Service
public class StoreService {
	private final StoreRepository storeRepository;
	private final HolidayStoreService holidayStoreService;
	
	public StoreService(StoreRepository storeRepository,HolidayStoreService holidayStoreService) {
		this.storeRepository = storeRepository;
		this.holidayStoreService = holidayStoreService;
	}
	 
	// 全ての民宿をページングされた状態で取得
	public Page<Store> findAllStores(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}
	
	// 指定キーワードを店舗名に含む民宿をページングされた状態で取得
	public Page<Store> findStoresByNameLike(String keyword, Pageable pageable) {
		return storeRepository.findByNameLike("%" + keyword + "%", pageable);
	}
	
	/* 指定したidを持つ民宿を取得
	 * Optional nullを持つ可能性のある（nullかもしれない）オブジェクトをより安全・便利に扱うためのクラス */
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
	
	// すべての店舗を作成日時が新しい順に並べ替え、ページングされた状態で取得する
    public Page<Store> findAllStoresByOrderByCreatedAtDesc(Pageable pageable) {
        return storeRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // すべての店舗を最低価格が安い順に並べ替え、ページングされた状態で取得する
    public Page<Store> findAllStoresByOrderByPriceMinAsc(Pageable pageable) {
        return storeRepository.findAllByOrderBypriceMinAsc(pageable);
    }
    
	// すべての店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
	public Page<Store> findAllStoresByOrderByAverageScoreDesc(Pageable pageable) {
		return storeRepository.findAllByOrderByAverageScoreDesc(pageable);
    }
	
	// すべての店舗を予約数が多い順に並べ替え、ページングされた状態で取得
	public Page<Store> findAllStoresByOrderByReservationCountDesc(Pageable pageable) {
		return storeRepository.findAllByOrderByReservationCountDesc(pageable);
	}
	
	// 指定されたキーワードを店舗名に含む店舗をページングされた状態で取得
	public Page<Store> findStoreByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable) {
		return storeRepository.findByNameLikeOrAddressLike("%" + nameKeyword + "%", "%" + addressKeyword + "%", pageable);
	}
	
	// 指定されたカテゴリを含む店舗をページングされた状態で取得
	public Page<Store> findStoreByCategoryLike(String category, Pageable pageable) {
		return storeRepository.findByCategoryLike(category, pageable);
	}
    
    // 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を作成日時が新しい順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable){
    	return storeRepository.findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(nameKeyword, addressKeyword, categoryNameKeyword, pageable);
    }
    
    // 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を最低価格が安い順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByNamelikeOrAddressLikeOrCategoryNameLikeOrderByPriceMinAsc(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable) {
    	return storeRepository.findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByPriceMinAsc(nameKeyword, addressKeyword, categoryNameKeyword, pageable);
    }
    
	// 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を平均評価が高い順に並べ替え、ページングされた状態で取得
	public Page<Store> findStoreByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable) {
    	return storeRepository.findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(nameKeyword, addressKeyword, categoryNameKeyword, pageable);
    }
	
	// 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を予約数が多い順に並べ替え、ページングされた状態で取得
	public Page<Store>findStoresByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(String namekeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable) {
		return storeRepository.findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(namekeyword, addressKeyword, categoryNameKeyword, pageable);
	}
    
    // 指定されたidのカテゴリが設定された店舗を作成日時が新しい順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByCategoryIdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable) {
    	return storeRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
    }
    
    // 指定されたidのカテゴリが設定された店舗を最低価格が安い順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByCategoryIdOrderByPriceMinAsc(Integer categoryId, Pageable pageable) {
    	return storeRepository.findByCategoryIdOrderByPriceMinAsc(categoryId, pageable);
    }
    
	// 指定されたidのカテゴリが設定された店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
	public Page<Store> findStoreByCategoryIdOrderByAverageScoreDesc(Integer categoryId, Pageable pageable) {
		return storeRepository.findByCategoryIdOrderByAverageScoreDesc(categoryId, pageable);
	}
	
	// 指定されたidのカテゴリが設定された店舗を予約数が多い順に並べ替え、ページングされた状態で取得
	public Page<Store> findStoresByCategoryIdOrderByReservationCountDesc(Integer categoryId, Pageable pageable) {
		return storeRepository.findByCategoryIdOrderByReservationCountDesc(categoryId, pageable);
	}
	
    // 指定された最低価格以下の店舗を作成日時が新しい順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByPriceMinThanEqualOrderByCreatedAtDesc(Integer PriceMin, Pageable pageable) {
    	return storeRepository.findByPriceMinThanEqualOrderByCreatedAtDesc(PriceMin, pageable);
    }
    
    // 指定された最低価格以下の店舗を最低価格が安い順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByPriceMinThanEqualOrderByPriceMinAsc(Integer PriceMin, Pageable pageable) {
    	return storeRepository.findByPriceMinThanEqualOrderByPriceMinAsc(PriceMin, pageable);
    }
    
    // 指定された最低価格以下の店舗を平均評価が高い順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByPriceMinThanEqualOrderByAverageScoreDesc(Integer price, Pageable pageable) {
    	return storeRepository.findByPriceMinThanEqualOrderByAverageScoreDesc(price, pageable);
    }
    
    // 指定された最低価格以下の店舗を予約数が多い順に並べ替え、ページングされた状態で取得
    public Page<Store> findStoresByPriceMinThanEqualOrderByReservationCountDesc(Integer price, Pageable pageable) {
    	return storeRepository.findByPriceMinThanEqualOrderByReservationCountDesc(price, pageable);
    }
    
    // 指定された店舗の定休日のday_indexフィールドの値をリストで取得
    public List<Integer> findDayIndexesByStoreId(Integer storeId) {
    	return storeRepository.findDayIndexesByStoreId(storeId);
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
		// Category category = storeRegisterForm.getCategory();
		List<Integer> holidayIds = storeRegisterForm.getHolidayIds();
		
		//System.out.println(store);
		
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
		//System.out.println(store);
		storeRepository.save(store);
		
		//if (category != null) {
			//CategoryService.
		//}
		
		if (holidayIds != null) {
			holidayStoreService.createHolidayStores(holidayIds, store);
		}
	}
	
	@Transactional
	public void updateStore(StoreEditForm storeEditForm, Store store) {
		MultipartFile imageFile = storeEditForm.getImageFile();
		List<Integer> holidayIds = storeEditForm.getHolidayIds();
		
		//System.out.println(store);
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			store.setImageName(hashedImageName);
		}
		
		store.setName(storeEditForm.getName());
		store.setCategory(storeEditForm.getCategory());
		store.setDescription(storeEditForm.getDescription());
		store.setStartTime(storeEditForm.getStartTime());
		store.setEndTime(storeEditForm.getEndTime());
		store.setPriceMin(storeEditForm.getPriceMin());
		store.setPriceMax(storeEditForm.getPriceMax());
		store.setAddress(storeEditForm.getAddress());
		store.setPhoneNumber(storeEditForm.getPhoneNumber());
		store.setCapacity(storeEditForm.getCapacity());
		//System.out.println(store);
		storeRepository.save(store);
		
		holidayStoreService.syncHolidayStores(holidayIds, store);
	}
	
	@Transactional
	public void deleteStore(Store store) {
		storeRepository.delete(store);
	}
	
	// UUIDを使って生成したファイル名を返す
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
