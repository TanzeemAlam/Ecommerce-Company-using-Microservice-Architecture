package com.tanzeem.cart_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanzeem.cart_service.dto.*;
import com.tanzeem.cart_service.entity.CartItem;
import com.tanzeem.cart_service.entity.UserCart;
import com.tanzeem.cart_service.enums.CartStatusEnum;
import com.tanzeem.cart_service.producer.InventoryProducer;
import com.tanzeem.cart_service.producer.OrderProducer;
import com.tanzeem.cart_service.repository.CartItemRepository;
import com.tanzeem.cart_service.repository.CartRepository;
import com.tanzeem.cart_service.service.CartService;
import com.tanzeem.cart_service.service.InventoryClient;
import com.tanzeem.cart_service.service.ProductDetailClient;
import com.tanzeem.cart_service.utility.AppConstant;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private InventoryClient inventoryClient;
	
	@Autowired
	private ProductDetailClient productDetailClient;
	
	@Autowired
	private InventoryProducer inventoryProducer;
	
	@Autowired
	private OrderProducer orderProducer;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private UserCart createCart(Long userId) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart == null) {																	//Create new cart if not present
			cart = new UserCart();
			
			cart.setUserId(userId);
			cart.setTotalAmount(0);
			cart.setStatus(CartStatusEnum.ACTIVE.toString());
			cart.setCreatedAt(LocalDateTime.now());
			
			cartRepository.save(cart);
			
			return cart;
		} 
		else if (cart.getStatus() == CartStatusEnum.ABANDONED.toString()) {					//If cart is abandoned, clear it and set it as ACTIVE
			clearAllCartItems(userId);
			
			cart.setStatus(CartStatusEnum.ACTIVE.toString());
			cart.setCreatedAt(getCurrentTime());
			
			cartRepository.save(cart);
		}
		
		return cart;
	}
	
	@Override
	@Transactional
	public String addCartItem(Long userId, CartAdjustmentDto dto, String token) {			//Using Transactional as 2 DB,  3 webClients, 1 kafka calls are executed synchronously
		//Create cart for user if doesn't exists
		UserCart cart = createCart(userId);
		
		CartItem cartItem = cartItemRepository.findByProductId(dto.getProductId());
		
		if (cartItem != null)	return AppConstant.ITEM_ALREADY_ADDED;
		else cartItem = new CartItem();
		
		//Hit Inventory Service to check if item exists with requested quantity
		String inventoryResponse = inventoryClient.validateItemFromInventory(dto, token);
			
		if (inventoryResponse.equalsIgnoreCase(AppConstant.VALID_INVENTORY_ITEM)) {
				
			Double pricePerUnit = productDetailClient.getProductPriceById(dto.getProductId(), token);
						
			cartItem.setCartId(cart.getCartId());
			cartItem.setProductId(dto.getProductId());
			cartItem.setQuantity(dto.getQuantity());
			cartItem.setPricePerUnit(pricePerUnit);
			cartItem.setAddedAt(LocalDateTime.now());
			
			cartItemRepository.save(cartItem);
			
			//Send async kafka event to inventory to reserve the product
			produceCartItemReserveKafkaEvent(dto);
			
			//Add cart item amount from total cart amount
			updateCartTotalAmount(cart, cartItem.getPricePerUnit() * cartItem.getQuantity(), AppConstant.CART_ADD);
			
			//Re-set Cart UpdatedAt field
			updateCartUpdatedAtField(cart);
			
			return AppConstant.ITEM_ADDED;
		}
			
		return inventoryResponse;		
	}

	@Override
	public String removeCartItem(Long userId, Long productId) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart != null) {
			CartItem cartItem = cartItemRepository.findByProductId(productId);
			
			if (cartItem != null) {
				cartItemRepository.delete(cartItem);
				
				//Send async call to inventory to release the product
				produceCartItemReleaseKafkaEvent(new CartAdjustmentDto(cartItem.getProductId(), cartItem.getQuantity()));
				
				//Remove cart item amount from total cart amount
				updateCartTotalAmount(cart, cartItem.getPricePerUnit() * cartItem.getQuantity(), AppConstant.CART_DELETE);
				
				//Re-set Cart UpdatedAt field
				updateCartUpdatedAtField(cart);
				
				deleteCartIfEmpty(cart.getCartId());
				
				return AppConstant.ITEM_DELETED;
			}
			
			return AppConstant.ITEM_NOT_FOUND;
		}
		
		return AppConstant.EMPTY_CART;
	}

	@Override
	public String updateCartItem(Long userId, CartAdjustmentDto dto) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart != null) {
			CartItem cartItem = cartItemRepository.findByProductId(dto.getProductId());
			
			if (cartItem != null) {
				Long quantityDifference = dto.getQuantity() - cartItem.getQuantity();
						
				cartItem.setQuantity(dto.getQuantity());
				cartItem.setUpdatedAt(getCurrentTime());
				
				cartItemRepository.save(cartItem);
				
				if (quantityDifference > 0) {
					dto.setQuantity(quantityDifference);
					
					produceCartItemReserveKafkaEvent(dto);
				} else {
					dto.setQuantity(Math.abs(quantityDifference));
					
					produceCartItemReleaseKafkaEvent(dto);
				}
				
				//Re-set Cart UpdatedAt field
				updateCartUpdatedAtField(cart);
				
				return AppConstant.ITEM_UPDATED;
			}
			
			return AppConstant.ITEM_NOT_FOUND;
		}
		
		return AppConstant.EMPTY_CART;
	}

	@Override
	public List<CartItemDto> getAllCartItems(Long userId) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart == null) return null;
		
		List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.getCartId());
		
		return convertToDtoList(cartItemList);
	}

	@Override
	@Transactional
	public String clearAllCartItems(Long userId) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart != null) {
			
			//Create cart item dto list to use after deletion
			List<CartAdjustmentDto> cartAdjustmentDtoList = createCartAdjustmentDtoList(cartItemRepository.findAllByCartId(cart.getCartId()));
					
			cartItemRepository.deleteAllByCartId(cart.getCartId());
			
			//Release all reserved products from inventory
			cartAdjustmentDtoList.forEach(cartAdjustmentDto -> produceCartItemReleaseKafkaEvent(cartAdjustmentDto));
			
			deleteCartIfEmpty(cart.getCartId());
			
			return AppConstant.CART_ITEMS_DISCARDED + " : " + AppConstant.CART_DELETED;
		}
		
		return AppConstant.EMPTY_CART;
	}

	@Override
	public String getTotalCartAmount(Long userId) {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart != null) {
			List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.getCartId());
			
			if (cartItemList != null) {
				double totalCartAmount = cartItemList.stream()
											.mapToDouble(cartItem -> cartItem.getPricePerUnit() * cartItem.getQuantity())
											.sum();
				
				return AppConstant.TOTAL_CART_AMOUNT + totalCartAmount;			
			}
			
			return AppConstant.EMPTY_CART;
		}
		
		return AppConstant.EMPTY_CART;
	}
	
	@Override
	@Transactional
	public void confirmCart(Long userId) throws JsonProcessingException {
		UserCart cart = cartRepository.findByUserId(userId);
		
		if (cart != null) {
			
			//Create cart item list to use after confirming product
			List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.getCartId());
			
			createCartAdjustmentDtoList(cartItemList).forEach(cartAdjustmentDto -> produceCartItemConfirmKafkaEvent(cartAdjustmentDto));
			
			OrderUpdateDto dto = new OrderUpdateDto();
			dto.setCartId(cart.getCartId());
			dto.setUserId(userId);
			dto.setTotalamount(cart.getTotalAmount());
			dto.setOrderedItemsJson(objectMapper.writeValueAsString(convertToDtoList(cartItemList)));
			
			//Produce Kafka event for Order
			produceOrderConfirmKafkaEvent(dto);
		}
	}
	
	/**********************************************************************PRIVATE HELPER METHODS**********************************************************************/
	
	/**
	 * Send async kafka event to inventory to reserve the product
	 */
	private void produceCartItemReserveKafkaEvent(CartAdjustmentDto dto) {
		try { 
			inventoryProducer.produceCartItemReserveKafkaEvent(
				AppConstant.RESERVE_CART_ITEM_QUANTITY, 
				dto, 
				AppConstant.ITEM_ADDED, 
				getCurrentTime()); 
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Send async kafka event to inventory to release the product
	 */
	private void produceCartItemReleaseKafkaEvent(CartAdjustmentDto dto) {
		try { 
			inventoryProducer.produceCartItemReleaseKafkaEvent(
					AppConstant.RELEASE_CART_ITEM_QUANTITY, 
					dto,
					AppConstant.ITEM_DELETED,
					getCurrentTime()); 
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Send async kafka event to inventory to confirm the product
	 */
	private void produceCartItemConfirmKafkaEvent(CartAdjustmentDto dto) {
		try { 
			inventoryProducer.produceCartItemConfirmKafkaEvent(
					AppConstant.CONFIRM_CART_ITEM_QUANTITY, 
					dto,
					AppConstant.ITEM_ADJUSTED,
					getCurrentTime()); 
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Send async kafka event to order service to confirm the order status
	 */
	private void produceOrderConfirmKafkaEvent(OrderUpdateDto dto) {
		try { 
			orderProducer.produceOrderConfirmKafkaEvent(
					AppConstant.CONFIRM_CART_ITEM_QUANTITY,
					dto,
					AppConstant.CONFIRM_ORDER,
					getCurrentTime());
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Create cart adjustment dto list for all deleted cart items
	 */
	private List<CartAdjustmentDto> createCartAdjustmentDtoList(List<CartItem> cartItemList) {
		return cartItemList.stream()
				.map(cartItem -> new CartAdjustmentDto(cartItem.getProductId(), cartItem.getQuantity()))
				.collect(Collectors.toList());
	}
	
	
	//Delete Empty Cart
	private void deleteCartIfEmpty(Long cartId) {
		List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cartId) ;
		
		if (cartItemList == null || cartItemList.isEmpty()) cartRepository.deleteByCartId(cartId);
	}
	
	//Update Cart Amount
	private void updateCartTotalAmount(UserCart cart, double amount, String cartAdd) {
		if (cartAdd.equals(AppConstant.CART_ADD)) cart.setTotalAmount(cart.getTotalAmount() + amount);
		else cart.setTotalAmount(cart.getTotalAmount() - amount);
		
		cart.setUpdatedAt(getCurrentTime());
		
		cartRepository.save(cart);
	}
	
	//Update cart field updated_At
	private void updateCartUpdatedAtField(UserCart cart) {
		cart.setUpdatedAt(getCurrentTime());
		
		cartRepository.save(cart);
	}
	
	private LocalDateTime getCurrentTime() { return LocalDateTime.now(); }
	
	/**
	 * Mapper methods 
	 */
	
	private List<CartItemDto> convertToDtoList(List<CartItem> cartItemList) {
		return cartItemList.stream()
				.map(cartItem -> mapper.map(cartItem, CartItemDto.class))
				.collect(Collectors.toList());
	}
}
