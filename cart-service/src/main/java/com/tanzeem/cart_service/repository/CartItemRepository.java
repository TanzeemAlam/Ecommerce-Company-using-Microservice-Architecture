package com.tanzeem.cart_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanzeem.cart_service.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	CartItem findByProductId(Long cardId);
	List<CartItem> findAllByCartId(Long cartId);
	
	void deleteAllByCartId(Long cartId);
}
