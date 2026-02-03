package io.github.samuel_pinheiro_c_lopes.spring_common.security.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.samuel_pinheiro_c_lopes.spring_common.security.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	private final JWTService jwtService;
	
	@Autowired
	public JWTAuthenticationFilter(final JWTService jwtService) {
		this.jwtService = jwtService;
	}
	
	@Override
	protected void doFilterInternal(
			final HttpServletRequest request, 
			final HttpServletResponse response, 
			final FilterChain filterChain
	) throws ServletException, IOException {
		final String requestURI = request.getRequestURI();
		
		// Skip JWT processing for login endpoint (may come through gateway with /userservice prefix)
		if (requestURI.endsWith("/authentication/login") || requestURI.endsWith("/user")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String token = this.retrieveToken(request);
		
		if (token != null)
			this.setContextAuthentication(token);
		
		filterChain.doFilter(request, response);
	}
	
	private void setContextAuthentication(final String token) {
		final String username = jwtService.getSubject(token);
		final List<SimpleGrantedAuthority> authorities = jwtService.getRoles(token)
				.stream()
				.map(r -> new SimpleGrantedAuthority(r))
				.toList(); 
		
		final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				username, 
				null, 
				authorities
		);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private String retrieveToken(final HttpServletRequest request) {
		final String token = request.getHeader(this.jwtService.AUTHORIZATION_HEADER);
		
		if (!this.jwtService.validateToken(token))
			return null;
		
		return token.substring(7);
	}
}
