package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	// カテゴリ名でカテゴリを検索しページングされた状態で取得
	public Page<Category> findByNameLike(String keyword, Pageable pageable);
    // idが最も大きいカテゴリを取得（idを基準に降順で並べ替え、最初の1件を取得
	public Category findFirstByOrderByIdDesc();
    // 指定したカテゴリ名を持つ最初のカテゴリを取得
    public Category findFirstByName(String name);
}