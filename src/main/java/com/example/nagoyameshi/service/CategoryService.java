package com.example.nagoyameshi.service;
 
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	// すべてのカテゴリをページングされた状態で取得
	
	
	// 指定されたキーワードをカテゴリ名に含むカテゴリをページングされた状態で取得
	
	
	// 指定したidを持つカテゴリを取得
	
	
	// カテゴリのレコード数を取得
	
	
	// idが最も大きいカテゴリを取得
	
	
	// フォームから送信されたカテゴリ情報をデータベースに登録
	
	
	// フォームから送信されたカテゴリ情報でデータベースを更新
	
	
	// 指定したカテゴリをデータベースから削除
	
}