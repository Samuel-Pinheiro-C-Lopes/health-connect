package io.github.samuel_pinheiro_c_lopes.spring_common.security.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTService {
	public final String AUTHORIZATION_HEADER =  "Authorization";
	public final String AUTHORIZATION_PREFIX = "Bearer";
	private final Algorithm algorithm;
	private final String secret;
	private final String issuer;
	
	public JWTService(
	        @Value("${app.security.token.secret}") final String secret,
	        @Value("${app.security.token.issuer}") final String issuer
        ) {
	        this.secret = secret;
	        this.issuer = issuer;
	        this.algorithm = Algorithm.HMAC256(this.secret); 
	    }
	
	public String getSubject(final String jwt) {
		try {
			return JWT.require(algorithm)
					.withIssuer(this.issuer)
					.build()
					.verify(jwt)
					.getSubject();
		} catch (JWTVerificationException ex) {
			throw new RuntimeException("Error when trying to verify JWT token: " + ex.getMessage());
		}
	}
	
	public List<String> getRoles(final String jwt) {
		try {
			DecodedJWT decodedJwt = JWT.require(algorithm)
					.withIssuer(this.issuer)
					.build()
					.verify(jwt);
			
	        if (decodedJwt.getClaim("roles").isNull()) 
	            return Collections.emptyList();
	        
			return decodedJwt.getClaim("roles").asList(String.class);
		} catch (JWTVerificationException ex) {
			throw new RuntimeException("Error when trying to verify JWT token: " + ex.getMessage());
		}
	}
	
	public String getToken(final UserDetails userDetails) {
		try {
			final List<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
			
			return JWT.create()
						.withIssuer(this.issuer)
						.withSubject(userDetails.getUsername())
						.withExpiresAt(this.getExpirationDate())
						.withClaim("roles", roles)
						.sign(algorithm);
		} catch (JWTCreationException ex) {
			throw new RuntimeException("Error when trying to generate JWT token for " + userDetails.getUsername() + ":" + ex.getMessage());
		}
	}
	
	public Boolean validateToken(String token) {
		return token != null && !token.isEmpty() && token.startsWith(AUTHORIZATION_PREFIX) && token.length() >= 7;
	}
	
	private Instant getExpirationDate() {
		return LocalDateTime
				.now()
				.plusHours(1l)
				.toInstant(ZoneOffset.of("-03:00"));
	}
}
