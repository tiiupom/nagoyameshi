package com.example.nagoyameshi.form;
 
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupForm {
	@NotBlank(message = "氏名")
	private String name;
	
	@NotBlank(message = "フリガナ")
	private String furigana;
	
	@NotBlank(message = "電話番号")
	private String phoneNumber;
	
	@NotBlank(message = "メールアドレス")
	@Email(message = "メールアドレスは正しい形式で入力してください")
	private String email;
	
	@NotBlank(message = "パスワード")
	@Length(min = 8, message = "パスワードは8文字以上で入力してください")
	private String password;
	
	@NotBlank(message = "パスワード（確認用）")
	private String passwordConfirmation;
}
