package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.VerificationToken;

// findByToken()でトークンに一致するデータをエンティティに格納されたテーブルから探す
public interface VerificationTokenRepository extends JpaRepository <VerificationToken, Integer> {
	public VerificationToken findByToken(String token);
}
