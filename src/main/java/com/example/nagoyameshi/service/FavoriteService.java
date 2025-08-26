package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	// 指定したidを持つお気に入りを取得
	public Optional<Favorite> findFavoriteById(Integer id) {
		return favoriteRepository.findById(id);
	}
	
	// 指定した店舗とユーザーが紐づいたお気に入りを取得
	public Favorite findFavoriteByStoreAndUser(Store store, User user) {
		return favoriteRepository.findByStoreAndUser(store, user);
	}
	
	// 指定したユーザーの全てのお気に入りを作成日時が新しい順に並べ替え、ページングされた状態で取得
	public Page<Favorite> findFavoritesByUserOrderByCreatedAtDesc(User user, Pageable pageable) {
		return favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
	}
	
	// お気に入りのレコード数を取得
	public long countFavorites() {
		return favoriteRepository.count();
	}
	
	// お気に入りをデータベースに登録
	public void createFavorite(Store store, User user) {
		Favorite favorite = new Favorite();
		
		favorite.setStore(store);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
	}
	
	// 指定したお気に入りをデータベースから削除
	@Transactional
	public void deleteFavorite(Favorite favorite) {
		favoriteRepository.delete(favorite);
	}
	
	// 指定したユーザーが指定した店舗をすでにお気に入りに追加済みかチェック
	// isFavorite()
	public boolean isFavorite(Store store, User user) {
		return favoriteRepository.findByStoreAndUser(store, user) != null;
	}
}
