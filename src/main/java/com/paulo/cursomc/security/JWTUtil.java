package com.paulo.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Resposavel para o TOKEN
 * @author Paulo Ferreira
 *
 */
@Component
public class JWTUtil {
	
	// Pegar palavras para CODIFIGAR
	@Value("${jwt.secret}")
	private String secret;
	
	// Tempo de Expiração
	@Value ("${jwt.expiration}")
	private Long expiration;
	
	/** Gera o Token
	 * 
	 * @param username - O Email
	 * @return String - Retornar o TOKEN
	 */
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	/** Verificar se o token e valido
	 * 
	 * @param token
	 * @return BOOLEN
	 */
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if(username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}
	
	// Pega o email atraveis do token
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} 
		catch (Exception e) {
			return null;
		}
	}

}
