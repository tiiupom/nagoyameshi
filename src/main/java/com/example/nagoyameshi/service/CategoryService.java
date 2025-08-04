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

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	// 全てのカテゴリをリスト形式で取得
	public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
	
	// 全てのカテゴリをページングされた状態で取得
	public Page<Category> findAllgetCategories(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	
	// 指定されたキーワードをカテゴリ名に含むカテゴリを、ページングされた状態で取得
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
		return categoryRepository.FindFirstByOrderByIdDesc();
	}
	
	// フォームから送信されたカテゴリ情報をデータベースに登録
	public void createCategories(CategoryRegisterForm categoryRegisterForm) {
		Category category = new Category();
	}
	
	// フォームから送信されたカテゴリ情報でデータベースを更新
	public void updateCategories(CategoryEditForm categoryEditForm, Category category) {
		category.setName(categoryEditForm.getName());
		
		categoryRepository.save(category);
	}
	
	// 指定したカテゴリをデータベースから削除
	public void deleteCategories(Category category) {
		categoryRepository.delete(category);
	}
}