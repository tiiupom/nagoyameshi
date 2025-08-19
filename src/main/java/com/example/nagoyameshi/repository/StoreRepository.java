package com.example.nagoyameshi.repository;
 
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
	public Page<Store> findByCategoryLike(String category, Pageable pageable);
	// すべての店舗を作成日が新しい順に並べ替えページングされた状態で表示
	public Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable);
	// すべての店舗を最低価格が低い順に並べ替えページングされた状態で表示
	public Page<Store> findAllByOrderBypriceMinAsc(Pageable pageable);
	
	// 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を作成日時が新しい順に並べ替え、ページングされた状態で表示
	@Query("SELECT DISTINCT r FROM Store r " +
			"LEFT JOIN r.categoriesStores cr " +
			"WHERE r.name LIKE %:name% " +
			"OR r.address LIKE %:address% " +
			"OR cr.category.name LIKE %:categoryName% " +
			"ORDER BY r.createdAt DESC")
	public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(@Param("name") String nameKeyword,
																							  @Param("address") String addressKeyword,
																							  @Param("categoryName") String categoryNameKeyword,
																							  Pageable pageable);
	
	// 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を最低価格が安い順に並べ替え、ページングされた状態で取得
	@Query("SELECT DISTINCT r FROM Store r " +
			"LEFT JOIN r.categoriesStores cr " +
			"WHERE r.name LIKE %:name% " +
			"OR r.address LIKE %:address% " +
			"OR cr.category.name LIKE %:categoryName% " +
			"ORDER BY r.PriceMin ASC")
	public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByPriceMinAsc(@Param("name") String nameKeyword,
																								@Param("address") String addressKeyword,
																								@Param("categoryName") String categoryNameKeyword,
	                                                                                            Pageable pageable);
	
    // 指定されたidのカテゴリが設定された店舗を作成日時が新しい順に並べ替え、ページングされた状態で取得する
    @Query("SELECT r FROM Store r " +
           "INNER JOIN r.categoriesStores cr " +
           "WHERE cr.category.id = :categoryId " +
           "ORDER BY r.createdAt DESC")
    public Page<Store> findByCategoryIdOrderByCreatedAtDesc(@Param("categoryId") Integer categoryId, Pageable pageable);

    // 指定されたidのカテゴリが設定された店舗を最低価格が安い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT r FROM Store r " +
           "INNER JOIN r.categoriesStores cr " +
           "WHERE cr.category.id = :categoryId " +
           "ORDER BY r.PriceMin ASC")
    public Page<Store> findByCategoryIdOrderByPriceMinAsc(@Param("categoryId") Integer categoryId, Pageable pageable);

    public Page<Store> findByPriceMinThanEqualOrderByCreatedAtDesc(Integer priceMin, Pageable pageable);
    public Page<Store> findByPriceMinThanEqualOrderByPriceMinAsc(Integer priceMin, Pageable pageable);
	
	// すべての店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT r FROM Store r " +
           "LEFT JOIN r.reviews rev " +
           "GROUP BY r.id " +
           "ORDER BY AVG(rev.score) DESC")
    public Page<Store> findAllByOrderByAverageScoreDesc(Pageable pageable);    

    // 指定されたキーワードを店舗名または住所またはカテゴリ名に含む店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT r FROM Store r " +
           "LEFT JOIN r.category cr " +
           "LEFT JOIN r.reviews rev " +
           "WHERE r.name LIKE %:name% " +
           "OR r.address LIKE %:address% " +
           "OR cr.category.name LIKE %:categoryName% " +
           "GROUP BY r.id " +
           "ORDER BY AVG(rev.score) DESC")
    public Page<Store> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(@Param("name") String nameKeyword,
    																						@Param("address") String addressKeyword,
    																						@Param("category") String categoryNameKeyword,
    																						Pageable pageable);
    
    // 指定されたidのカテゴリが設定された店舗を平均評価が高い順に並べ替え、ページングされた状態で取得する
    @Query("SELECT r FROM Store r " +
           "INNER JOIN r.category cr " +
           "LEFT JOIN r.reviews rev " +
           "WHERE cr.category.id = :categoryId " +
           "GROUP BY r.id " +
           "ORDER BY AVG(rev.score) DESC")
    public Page<Store> findByCategoryIdOrderByAverageScoreDesc(@Param("categoryId") Integer categoryId, Pageable pageable);    
    
}
