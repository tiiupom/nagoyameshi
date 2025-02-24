package com.example.nagoyameshi.service;
 
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Category;
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

	// 指定したカテゴリ名を持つ最初のカテゴリを取得する
    public Category findFirstCategoryByName(String name) {
        return categoryRepository.findFirstByName(name);
    }
}
