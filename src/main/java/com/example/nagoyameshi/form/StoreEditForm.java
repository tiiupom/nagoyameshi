package com.example.nagoyameshi.form;

import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreEditForm {
	@NotBlank(message = "店舗名を入力してください")
	private String name;
	 
	private MultipartFile imageFile;
	
	@NotBlank(message = "カテゴリーを選択してください")
	private Category category;
	
	@NotBlank(message = "店舗説明を入力してください")
	private String description;
	
	@NotNull(message = "開店時間を選択してください")
	private LocalTime startTime;
	
	@NotNull(message = "閉店時間を選択してください")
	private LocalTime endTime;
	
	@NotNull(message = "最低価格を選択してください")
	private Integer priceMin;
	
	@NotNull(message = "最高価格を選択してください")
	private Integer priceMax;
	
	@NotBlank(message = "住所を入力してください")
	private String address;
	
	@NotBlank(message = "電話番号を入力してください")
	private String phoneNumber;
	
	@Min(value = 1, message = "最大利用人数は1人以上に設定してください。")
	private Integer capacity;
}

