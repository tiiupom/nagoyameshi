package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import com.example.nagoyameshi.entity.Holiday;
import com.example.nagoyameshi.repository.HolidayRepository;

public class HolidayService {
	private final HolidayRepository holidayRepository;
	
	public HolidayService(HolidayRepository holidayRepository) {
		this.holidayRepository = holidayRepository;
	}
	
	// 指定したidを持つ定休日を取得
	public Optional<Holiday> findHolidayByID(Integer id) {
		return holidayRepository.findById(id);
	}
	
	// すべての定休日をリスト形式で取得
	public List<Holiday> findAllHolidays() {
		return holidayRepository.findAll();
	}
}
