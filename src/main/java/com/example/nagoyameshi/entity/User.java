// 認証用のエンティティ
package com.example.nagoyameshi.entity;
 
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/* @Entity	：　クラスがエンティティとして機能する
 * @Table	：　エンティティにマッピングするテーブルの指定
 * @Data	：　ゲッターやセッターを自動生成する
 * @Id		：　フィールドを主キーに指定する
 * @GeneratedValue & strategy = GenerationType.IDENTITY
 * 				：　AUTO_INCREMENTを指定したidカラムを利用し値を自動生成する
 * @Column	:　フィールドにマッピングするカラム名を指定
 * @ManyToOne	:　多対一のリレーションシップを表現
 * @JoinColumn	:　name属性に外部キーのカラム名を指定 
 * insertable	:　カラムに値を挿入できるかどうか指定
 * updatable	:　カラムの値を更新できるかどうかを指定
 * 	デフォルト値はtrue（アプリ側でカラムに挿入・更新を管理）
 * 	falseでデータベースでカラムに挿入・更新を管理
 * 	これによりデータベース側に設定したデフォルト値CURRENT＿TIMESTAMPが自動的に挿入される */
@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "furigana")
	private String furigana;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
	
	@Column(name = "stripe_customer_id")
	private String stripeCustomerId;
}
