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
		cartService.addCartItem(userId, dto, authHeader);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(AppConstant.ITEM_ADDED));
	}
	
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponse> removeItemFromCart(@Valid @PathVariable Long userId, @Valid @PathVariable Long itemId) {
		cartService.removeCartItem(userId, itemId);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(AppConstant.ITEM_ADDED));
	}
	
	@PutMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponse> updateItemFromCart(@Valid @PathVariable Long userId, @Valid @PathVariable Long itemId, @Valid @RequestBody CartAdjustmentDto dto) {
		String response = cartService.updateCartItem(userId, itemId, dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(response));
	}
	
	@GetMapping("/{userId}")
	public List<CartItemDto> getCart(@Valid @PathVariable Long userId) {
		return cartService.getAllCartItems(userId);
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
