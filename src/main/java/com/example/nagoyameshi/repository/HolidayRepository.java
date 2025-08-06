package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
	

}
