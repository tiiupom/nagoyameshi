package com.example.nagoyameshi.repository;
 
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
	public Page<Store> findByNameLike(String keyword, Pageable pageable);
	// idカラムの値で降順に並べ替え最初の１件を取得するフォーム
	public Store findFirstByOrderByIdDesc();
	// 店舗名または住所で検索するフォーム
	public Page<Store> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);
	// カテゴリで検索するフォーム
	public Page<Store> findByCategoryIdLike(String category, Pageable pageable);
	// すべての店舗を作成日が新しい順に並べ替えページングされた状態で表示
	public Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable);
	// すべての店舗を最低価格が低い順に並べ替えページングされた状態で表示
	public Page<Store> findAllByOrderByPriceMinAsc(Pageable pageable);
	
	// すべての店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT s FROM Store s " +
           "LEFT JOIN s.reviews rev " +
           "GROUP BY s.id " +
           "ORDER BY AVG(rev.score) DESC")
    public Page<Store> findAllByOrderByAverageScoreDesc(Pageable pageable);    
	
	// 全ての店舗を予約数が多い順に並べ替え、ページングされた状態で取得
    @Query("SELECT s FROM Store s " +
    		"LEFT JOIN s.reservations res " +
    		"GROUP BY s.id " +
    		"ORDER BY COUNT(res) DESC")
    public Page<Store> findAllByOrderByReservationCountDesc(Pageable pageable);
    
    // 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を作成日時が新しい順に並べ替え、ページングされた状態で表示
	@Query("SELECT DISTINCT s FROM Store s " +
			"LEFT JOIN s.categoryStores cs " +
			"WHERE s.name LIKE %:name% " +
			"OR s.address LIKE %:address% " +
			"OR cs.category.name LIKE %:categoryName% " +
			"ORDER BY s.createdAt DESC")
	public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(@Param("name") String nameKeyword,
																						@Param("address") String addressKeyword,
																						@Param("categoryName") String categoryNameKeyword,
																						Pageable pageable);
	
	// 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を最低価格が安い順に並べ替え、ページングされた状態で取得
	@Query("SELECT DISTINCT s FROM Store s " +
			"LEFT JOIN s.categoryStores cs " +
			"WHERE s.name LIKE %:name% " +
			"OR s.address LIKE %:address% " +
			"OR cs.category.name LIKE %:categoryName% " +
			"ORDER BY s.priceMin ASC")
	public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByPriceMinAsc(@Param("name") String nameKeyword,
																						@Param("address") String addressKeyword,
																						@Param("categoryName") String categoryNameKeyword,
																						Pageable pageable);
	
	// 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT s FROM Store s " +
           "LEFT JOIN s.category cs " +
           "LEFT JOIN s.reviews rev " +
           "WHERE s.name LIKE %:name% " +
           "OR s.address LIKE %:address% " +
           "OR cs.category.name LIKE %:categoryName% " +
           "GROUP BY s.id " +
           "ORDER BY AVG(rev.score) DESC")
    public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(@Param("name") String nameKeyword,
																							@Param("address") String addressKeyword,
																							@Param("categoryName") String categoryNameKeyword,
																							Pageable pageable);

    // 指定されたキーワードを店舗または住所またはカテゴリ名に含む店舗を予約数が多い順に並べ替え、ページングされた状態で取得
    @Query("SELECT s FROM Store s " +
    		"LEFT JOIN s.categoryStores cs " +
    		"LEFT JOIN s.reservations res " +
    		"WHERE s.name LIKE :name " +
    		"OR s.address LIKE :address " +
    		"OR cs.category.name LIKE :categoryName " +
    		"GROUP BY s.id " +
    		"ORDER BY COUNT(DISTINCT res.id) DESC")
    public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(@Param("name") String nameKeyword,
    																							@Param("address") String addressKeyword,
    																							@Param("categoryName") String categoryNameKeyword,
    																							Pageable pageable);
    
    // 指定されたidのカテゴリが設定された店舗を作成日時が新しい順に並べ替え、ページングされた状態で取得する
    @Query("SELECT s FROM Store s " +
    		"INNER JOIN s.categoryStores cs " +
    		"WHERE cs.category.id = :categoryId " +
    		"ORDER BY s.createdAt DESC")
    public Page<Store> findByCategoryIdOrderByCreatedAtDesc(@Param("categoryId") Integer categoryId, Pageable pageable);

    // 指定されたidのカテゴリが設定された店舗を最低価格が安い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT s FROM Store s " +
           "INNER JOIN s.categoryStores cs " +
           "WHERE cs.category.id = :categoryId " +
           "ORDER BY s.priceMin ASC")
    public Page<Store> findByCategoryIdOrderByPriceMinAsc(@Param("categoryId") Integer categoryId, Pageable pageable);

    // 指定されたidのカテゴリが設定された店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT s FROM Store s " +
    		"INNER JOIN s.categoryStores cs " +
            "LEFT JOIN s.reviews rev " +
            "WHERE cs.category.id = :categoryId " +
            "GROUP BY s.id " +
            "ORDER BY AVG(rev.score) DESC")
    public Page<Store> findByCategoryIdOrderByAverageScoreDesc(@Param("categoryId") Integer categoryId, Pageable pageable);    

    // 指定されたidのカテゴリが設定された店舗を予約数が多い順に並べ替え、ページングされた状態で表示
    @Query("SELECT s FROM Store s " +
    		"INNER JOIN s.categoryStores cs " +
            "LEFT JOIN s.reservations res " +
            "WHERE cs.category.id = :categoryId " +
            "GROUP BY s.id " +
            "ORDER BY COUNT(res) DESC")
    public Page<Store> findByCategoryIdOrderByReservationCountDesc(@Param("categoryId") Integer categoryId, Pageable pageable);
    
    public Page<Store> findByPriceMinLessThanEqualOrderByCreatedAtDesc(Integer priceMin, Pageable pageable);
    public Page<Store> findByPriceMinLessThanEqualOrderByPriceMinAsc(Integer priceMin, Pageable pageable);
  
    // 指定された最低価格以下の店舗を平均評価が高い順に並べ替え、ページングされた状態で取得
    @Query("SELECT s FROM Store s " +
    		"LEFT JOIN s.reviews rev " +
    		"WHERE s.priceMin <= :price " +
    		"GROUP BY s.id " +
    		"ORDER BY AVG(rev.score) DESC")
    public Page<Store> findByPriceMinLessThanEqualOrderByAverageScoreDesc(@Param("price") Integer price, Pageable pageable);
    
    // 指定された最低価格以下の店舗を予約数が多い順に並べ替え、ページングされた状態で取得
    @Query("SELECT s FROM Store s " +
    		"LEFT JOIN s.reservations res " +
    		"WHERE s.priceMin <= :price " +
    		"GROUP BY s.id " +
    		"ORDER BY COUNT(res) DESC")
    public Page<Store> findByPriceMinLessThanEqualOrderByReservationCountDesc(@Param("price") Integer price, Pageable pageable);
    
    // 指定された店舗の定休日のday_indexフィールドの値をリストで取得
    @Query("SELECT h.dayIndex FROM Holiday h " +
    		"INNER JOIN h.holidayStores hs " +
    		"INNER JOIN hs.store s " +
    		"WHERE s.id = :storeId")
    public List<Integer> findDayIndexByStoreId(@Param("storeId") Integer storeId);
}
