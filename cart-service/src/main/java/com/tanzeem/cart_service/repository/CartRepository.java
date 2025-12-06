package com.tanzeem.cart_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanzeem.cart_service.entity.UserCart;

@Repository
public interface CartRepository extends JpaRepository<UserCart, Long>{
	UserCart findByUserId(Long userId);
}
