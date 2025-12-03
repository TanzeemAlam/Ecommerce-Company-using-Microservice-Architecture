package com.tanzeem.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.tanzeem.api_gateway.util.*;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	public static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	@Autowired
	private RouteValidator validator;
	
	@Autowired
	private WebClient webClient;
	
	public AuthenticationFilter() {
		super(Config.class);
	}

	
	@Override
	public GatewayFilter apply(Config config) {
		
		return ((exchange, chain) -> {
			
			if (validator.isSecured.test(exchange.getRequest())) {
				
				logger.info("Validating JWT token.....");
				
				//Check if Header contains JWT token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException(AppConstants.MISSING_AUTH_HEADER);
				}
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);				

				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				
				//Validating token using WebClient
				return webClient.get()
						.uri("http://localhost:8081/users/validateToken?token=" + authHeader)
						.retrieve()
						.bodyToMono(ApiResponse.class)
						.flatMap(response -> handleResponse(response, exchange, chain))
						.onErrorResume(e -> handleError(exchange));
			}
			else	logger.info("Open request! No JWT required...");
			
			return chain.filter(exchange);
		});
	}

	/*
	 * Handling response got from web client
	 */
	private Mono<Void> handleResponse(ApiResponse apiResponse, ServerWebExchange exchange, GatewayFilterChain chain) {
        if (apiResponse == null || apiResponse.getMessage() == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            
            return exchange.getResponse().setComplete();
        }
        
        return chain.filter(exchange);
    }

	/*
	 * Handling error got from web client
	 */
    private Mono<Void> handleError(ServerWebExchange exchange) {
    	
    	logger.info("Invalid JWT token!!!");
    	
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        
        return exchange.getResponse().setComplete();
    }

	public static class Config {
		
	}
}
