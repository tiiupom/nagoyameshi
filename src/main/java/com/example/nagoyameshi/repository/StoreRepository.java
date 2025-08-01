package com.example.nagoyameshi.repository;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
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
	
	public Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
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
    																						@Param("categoryName") String categoryNameKeyword,
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
