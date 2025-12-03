package com.tanzeem.product_detail_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

import com.tanzeem.product_detail_service.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private static final String BASE_URL = "/product-details/**";
	private static final String BASE_URL_WITHOUT_STAR = "/product-details";
	
	
	@Autowired private JwtFilter jwtFilter;
	  
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
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}
}
