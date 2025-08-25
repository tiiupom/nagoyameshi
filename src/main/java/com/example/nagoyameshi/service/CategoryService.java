package com.example.nagoyameshi.service;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	// すべてのカテゴリをページングされた状態で取得
	public Page<Category> findAllCategories(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	
	// 指定されたキーワードをカテゴリ名に含むカテゴリをページングされた状態で取得
	public Page<Category> findCategoriesByNameLike(String keyword, Pageable pageable) {
		return categoryRepository.findByNameLike("%" + keyword + "%", pageable);
	}
	
	// 指定したidを持つカテゴリを取得
	public Optional<Category> findCategoryById(Integer id) {
		return categoryRepository.findById(id);
	}
	
	// カテゴリのレコード数を取得
	public long countCategories() {
		return categoryRepository.count();
	}
	
	// idが最も大きいカテゴリを取得
	public Category findFirstCategoryByOrderByIdDesc() {
		return categoryRepository.findFirstByOrderByIdDesc();
	}
	
	// 全てのカテゴリをリスト形式で取得
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();
	}
	
	// フォームから送信されたカテゴリ情報をデータベースに登録
	@Transactional
	public void createCategory(CategoryRegisterForm categoryRegisterForm) {
		Category category = new Category();
		
		category.setName(categoryRegisterForm.getName());
		
		categoryRepository.save(category);
	}
	
	// フォームから送信されたカテゴリ情報でデータベースを更新
	@Transactional
	public void updateCategory(CategoryEditForm categoryEditForm, Category category) {
		category.setName(categoryEditForm.getName());
		
		categoryRepository.save(category);
	}
	
	// 指定したカテゴリをデータベースから削除
	@Transactional
	public void deleteCategory(Category category) {
		categoryRepository.delete(category);
	   }
}