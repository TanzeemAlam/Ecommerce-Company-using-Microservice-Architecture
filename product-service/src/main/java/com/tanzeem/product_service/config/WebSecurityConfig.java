package com.tanzeem.product_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

import com.tanzeem.product_service.filter.JwtFilter;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	@Autowired private JwtFilter jwtFilter;
	
	private static final String BASE_URL = "/products/**";
	private static final String BASE_URL_WITHOUT_STAR = "/products";
	  
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(
					auth -> 
						auth.requestMatchers(BASE_URL_WITHOUT_STAR + "/config").permitAll()
							.requestMatchers(HttpMethod.GET, BASE_URL).hasAnyRole("USER", "ADMIN")
							.requestMatchers(HttpMethod.PUT, BASE_URL).hasAnyRole("USER", "ADMIN")
							.requestMatchers(HttpMethod.POST, BASE_URL).hasRole("ADMIN")
							.requestMatchers(HttpMethod.DELETE, BASE_URL).hasRole("ADMIN")
							.anyRequest().authenticated() )
			.addFilterBefore(jwtFilter, AuthorizationFilter.class);
	  
		  return http.build();
	}
	 
	
	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
