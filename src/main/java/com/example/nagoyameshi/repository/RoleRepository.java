// rolesテーブルとやり取りする認可用のリポジトリ
package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Role;


/* JpaRepositoryインターフェースを継承し基本的なCRUD操作を行うメソッドを有効にする
　　　Spring Data JPA（データベースとのやり取りを簡単にするフレームワーク）により提供されるインターフェース*/
public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByName(String name);
}
