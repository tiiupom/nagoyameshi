package com.example.nagoyameshi.form;

import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreRegisterForm {
	@NotBlank(message = "店舗名")
	private String name;
	
	private MultipartFile imageFile;
	
	@NotBlank(message = "店舗説明")
	private String description;
	
	@NotNull(message = "開店時間")
	private LocalTime startTime;
	
	@NotNull(message = "閉店時間")
	private LocalTime endTime;
	
	@NotNull(message = "最低価格")
	private Integer priceMin;
	
	@NotNull(message = "最高価格")
	private Integer priceMax;
	
	@NotBlank(message = "住所")
	private String address;
	
	@NotBlank(message = "電話番号")
	private String phoneNumber;
	
	@NotBlank(message = "定休日")
	private String holidays;
	
	@NotNull(message = "最大利用人数")
	@Min(value = 1, message = "最大利用人数は1人以上に設定してください。")
	private Integer capacity;
}
