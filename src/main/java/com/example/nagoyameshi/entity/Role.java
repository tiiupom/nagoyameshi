// 認可用のエンティティ
package com.example.nagoyameshi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/* @Entity	：　クラスがエンティティとして機能する
 * @Table	：　エンティティにマッピングするテーブルの指定
 * @Data	：　ゲッターやセッターを自動生成する
 * @Id		：　フィールドを主キーに指定する
 * @GeneratedValue & strategy = GenerationType.IDENTITY
 * 				：　AUTO_INCREMENTを指定したidカラムを利用し値を自動生成する
 * @Column	:　フィールドにマッピングするカラム名を指定
 */
@Entity
@Table(name = "roles")
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
}
