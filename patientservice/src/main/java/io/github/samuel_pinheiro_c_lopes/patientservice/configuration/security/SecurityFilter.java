package io.github.samuel_pinheiro_c_lopes.patientservice.configuration.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.samuel_pinheiro_c_lopes.patientservice.services.JWTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	private final String AUTHORIZATION_HEADER =  "Authorization";
	private final String AUTHORIZATION_PREFIX = "Bearer";
	
	private final JWTokenService jwtokenService;
	
	@Autowired
	public SecurityFilter(final JWTokenService jwtokenService) {
		this.jwtokenService = jwtokenService;
	}
	
	@Override
	protected void doFilterInternal(
			final HttpServletRequest request, 
			final HttpServletResponse response, 
			final FilterChain filterChain
	) throws ServletException, IOException {
		final String token = this.retrieveToken(request);
		
		if (token != null)
			this.setContextAuthentication(token);
		
		filterChain.doFilter(request, response);
	}
	
	private void setContextAuthentication(final String token) {
		final String username = jwtokenService.getSubject(token);
		final List<SimpleGrantedAuthority> authorities = jwtokenService.getRoles(token)
				.stream()
				.map(r -> new SimpleGrantedAuthority(r))
				.toList(); 
		
		final var authentication = new UsernamePasswordAuthenticationToken(
				username, 
				null, 
				authorities
		);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private String retrieveToken(final HttpServletRequest request) {
		final String token = request.getHeader(AUTHORIZATION_HEADER);
		
		if (token == null || token.isEmpty() || !token.startsWith(AUTHORIZATION_PREFIX) || token.length() < 7)
			return null;
		
		return token.substring(7);
	}
}
