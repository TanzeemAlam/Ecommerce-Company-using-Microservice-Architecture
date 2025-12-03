package com.tanzeem.product_service.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanzeem.product_service.dto.*;
import com.tanzeem.product_service.entity.Product;
import com.tanzeem.product_service.producer.NotificationProducer;
import com.tanzeem.product_service.repository.ProductRepository;
import com.tanzeem.product_service.service.*;
import com.tanzeem.product_service.util.AppConstants;

@Service
public class ProductServiceImpl implements ProductService{

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ProductDetailClient productDetailClient;
	
	@Autowired
	private NotificationProducer kafkaService;
	
	@Autowired
	private InventoryClient inventoryClient;
	
	@Override
	public void addProduct(Product p, String token) {
		p.setCreatedAt(LocalDateTime.now());
		
		productRepository.save(p);
		
		inventoryClient.addProductInInventory(new InventoryDto(p.getId(), p.getSku()), token);	//Calling inventory service to add product
		
		try {
			kafkaService.produceKafkaEvent(AppConstants.KAFKA_PRODUCT_REGISTERED_EVENT,
								p.getName(), 
								AppConstants.PRODUCT_CREATED,
								LocalDateTime.now());
		} catch (Exception e) {
			logger.error(AppConstants.KAFKA_ERROR +  e.getMessage());
		}
	}

	@Override
	public List<ProductResponseDto> getAllProducts(String token) {
		List<Product> productList = productRepository.findAll();
		
		//Getting all product details and converting it to map for quick look-up
		List<ProductDetailDto> productDetailList = productDetailClient.getAllProductDetails(token);
		
		Map<Long, ProductDetailDto> productDetailsMap = productDetailList.stream()
															.collect(Collectors.toMap(ProductDetailDto::getProductId, p -> p));
		
		//Performing stream and merge product and productDetail into single productResponseDto object
		return productList.stream()
				.map(product -> {
					return convertToResponseDto(product, productDetailsMap.get(product.getId()));
				})
				.collect(Collectors.toList());
	}

	@Override
	public ProductResponseDto getProduct(Long id, String token) {
		Product product = productRepository.findById(id).orElse(null);
		
		if (product != null) {	
			ProductDetailDto productDetail = productDetailClient.getProductDetailsById(id, token);
			
			return convertToResponseDto(product, productDetail);
		}
		
		return null;
	}

	@Override
	public String updateProduct(Long id, Product updatedProduct) {
		Product p = productRepository.findById(id).orElse(null);
		
		if (p != null) { 
			p.setName(updatedProduct.getName());
			p.setDescription(updatedProduct.getDescription());
			p.setUpdatedAt(LocalDateTime.now());													//Product Updated Date/Time
			
			
			productRepository.save(p);
			
			return AppConstants.PRODUCT_UPDATED;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public String deleteProduct(Long id) {
		Product p = productRepository.findById(id).orElse(null);
		
		if (p != null) {
			productRepository.delete(p);
			
			return AppConstants.PRODUCT_DELETED;
		}
		
		return AppConstants.NOT_FOUND;
	}

	@Override
	public Boolean checkCustomerExists(Long id) {
		return productRepository.existsById(id);
	}
	
	/**
	 * Model Mapper
	 * Product Entity & ProductDetail DTO to ProductResponse DTO conversion using mapper
	 */
	private ProductResponseDto convertToResponseDto(Product product, ProductDetailDto productDetail) {
		ProductResponseDto responseDto = mapper.map(product, ProductResponseDto.class);
		
		if (productDetail != null) {
			responseDto.setSize(productDetail.getSize());
			responseDto.setPrice(productDetail.getPrice());
			responseDto.setDesign(productDetail.getDesign());
		}
		
		return responseDto;
	}
}
