package com.tanzeem.cart_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tanzeem.cart_service.dto.*;
import com.tanzeem.cart_service.service.CartService;
import com.tanzeem.cart_service.utility.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Value("${custom.message}")
	private String customMessage;
	
	@GetMapping("/config")
	public String getConfig() {
		return "App Name from Cart Service(using public config): " + customMessage;
	}
	
	@PostMapping("/{userId}/items")
	public ResponseEntity<ApiResponse> addItemToCart(@Valid @PathVariable Long userId, @Valid @RequestBody CartAdjustmentDto dto, @RequestHeader("Authorization") String authHeader) {
		String response = cartService.addCartItem(userId, dto, authHeader);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@DeleteMapping("/{userId}/items/{productId}")
	public ResponseEntity<ApiResponse> removeItemFromCart(@Valid @PathVariable Long userId, @Valid @PathVariable Long productId) {
		String response = cartService.removeCartItem(userId, productId);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@PutMapping("/{userId}/items")
	public ResponseEntity<ApiResponse> updateItemFromCart(@Valid @PathVariable Long userId, @Valid @RequestBody CartAdjustmentDto dto) {
		String response = cartService.updateCartItem(userId, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getCart(@Valid @PathVariable Long userId) {
		List<CartItemDto> cartItemList = cartService.getAllCartItems(userId);
		
		if (cartItemList == null) return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(AppConstant.EMPTY_CART));
		
		return ResponseEntity.status(HttpStatus.OK).body(cartItemList);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> clearCart(@Valid @PathVariable Long userId) {
		String response = cartService.clearAllCartItems(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{userId}/amount")
	public ResponseEntity<ApiResponse> getTotalCartAmount(@Valid @PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(cartService.getTotalCartAmount(userId)));
	}
}
