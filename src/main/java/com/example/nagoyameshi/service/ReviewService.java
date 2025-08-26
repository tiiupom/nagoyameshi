package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	// 指定したidを持つレビューを取得
	public Optional<Review> findReviewById(Integer id) {
		return reviewRepository.findById(id);
	}
	
	// 指定した店舗の全てのレビューを作成日時が新しい順位並べ替え、ページングされた状態で取得
	public Page<Review> findReviewsByStoreOrderByCreatedAtDesc(Store store, Pageable pageable) {
		return reviewRepository.findByStoreOrderByCreatedAtDesc(store, pageable);
	}
	
	// レビューのレコード数を取得
	public long countReviews() {
		return reviewRepository.count();
	}
	
	// idが最も大きいレビューを取得
	public Review findFirstReviewByOrderByIdDesc() {
		return reviewRepository.findFirstByOrderByIdDesc();
	}
	
	// フォームから送信されたレビューをデータベースに登録
	public void createReview(ReviewRegisterForm reviewRegisterForm, Store store, User user) {
		Review review = new Review();
		
		review.setContent(reviewRegisterForm.getContent());
		review.setScore(reviewRegisterForm.getScore());
		review.setStore(store);
		review.setUser(user);
		
		reviewRepository.save(review);
	}
	
	// フォームから送信されたレビューでデータベースを更新
	@Transactional
	public void updateReview(ReviewEditForm reviewEditForm, Review review) {
		review.setScore(reviewEditForm.getScore());
		review.setContent(reviewEditForm.getContent());
		
		reviewRepository.save(review);
	}
	
	// 指定したレビューをデータベースから削除
	@Transactional
	public void deleteReview(Review review) {
		reviewRepository.delete(review);
	}
	
	// 指定したユーザーが指定した店舗のレビューを既に投稿済みかチェック
	public boolean hasUserAlreadyReviewed(Store store, User user) {
		return reviewRepository.findByStoreAndUser(store, user) != null;
	}
	
}
