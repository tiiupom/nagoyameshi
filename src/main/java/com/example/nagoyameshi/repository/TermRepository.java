package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Term;

public interface TermRepository extends JpaRepository<Term, Integer>{

	// idが最も大きい利用規約を取得（idを基準に降順で並べ替え、最初の1件を取得）
	public Term findFirstByOrderByIdDesc();
}
