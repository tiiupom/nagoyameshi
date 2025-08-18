package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyEditForm {

	@NotBlank(message = "会社名を入力してください。")
	private String name;
	
	@NotBlank(message = "所在地を入力してください。")
	private String address;
	
	@NotBlank(message = "代表者を入力してください。")
	private String representative;
	
	@NotBlank(message = "設立年月日を入力してください。")
	private String establishmentDate;
	
	@NotBlank(message = "資本金を入力してください。")
	private String capital;
	
	@NotBlank(message = "事業内容を入力してください。")
	private String business;
	
	@NotBlank(message = "従業員数を入力してください。")
	private String numberOfEmployees;
}
