package com.example.nagoyameshi.entity;
 
import java.sql.Timestamp;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stores")
@Data
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	 
	@ManyToOne
	@JoinColumn(name = "category")
	private Category category;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "image_name")
	private String imageName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "start_time")
	private LocalTime startTime;
	
	@Column(name = "end_time")
	private LocalTime endTime;
	
	@Column(name = "price_min")
	private Integer priceMin;
	
	@Column(name = "price_max")
	private Integer priceMax;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "holidays")
	private String holidays;
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
	
}
