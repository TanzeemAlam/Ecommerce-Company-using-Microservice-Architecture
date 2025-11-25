package com.tanzeem.user_service.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";
	
	private SecretKey getSigningKey() {	
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

	private String createToken(Map<String, Object> jwtClaims, String username) {
		return Jwts.builder()
				.claims(jwtClaims)
				.subject(username)
				.issuer("ecommerce-application")
				.header().empty().add("typ", "JWT")
				.and()
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(getSigningKey())
				.compact();
		
	}
	
	public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
		Map<String, Object> jwtClaims = new HashMap<>();
		
		List<String> roles = authorities.stream()
											.map(GrantedAuthority::getAuthority)
											.toList();
		
		jwtClaims.put("roles", roles);
		
		return createToken(jwtClaims, username);
	}

	public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
