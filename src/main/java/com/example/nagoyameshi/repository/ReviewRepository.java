package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
   public Review findByStoreAndUser(Store store, User user);
   public Page<Review> findByStoreOrderByCreatedAtDesc(Store store, Pageable pageable);
   public Review findFirstByOrderByIdDesc();
}