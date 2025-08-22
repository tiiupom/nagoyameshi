// usersテーブルとやり取りする認証用のリポジトリ
package com.example.nagoyameshi.repository;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

/* JpaRepositoryインターフェースを継承し基本的なCRUD操作を行うメソッドを有効にする
　　　Spring Data JPA（データベースとのやり取りを簡単にするフレームワーク）により提供されるインターフェース*/
public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByEmail(String email);
	
	public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
	
	// 指定したロール名に紐づくユーザーのレコード数を取得
	public long countByRole_Name(String roleName);
}
