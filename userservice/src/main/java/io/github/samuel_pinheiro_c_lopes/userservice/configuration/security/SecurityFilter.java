package io.github.samuel_pinheiro_c_lopes.userservice.configuration.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.UserRepository;
import io.github.samuel_pinheiro_c_lopes.userservice.services.JWTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	private final String AUTHORIZATION_HEADER =  "Authorization";
	private final String AUTHORIZATION_PREFIX = "Bearer";
	
	private final JWTokenService jwtokenService;
	private final UserRepository userRepository;
	
	@Autowired
	public SecurityFilter(final JWTokenService jwtokenService, final UserRepository userRepository) {
		this.jwtokenService = jwtokenService;
		this.userRepository = userRepository;
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
		
		final User user = this.userRepository.findUserByEmail(username);
		
		final var authentication = new UsernamePasswordAuthenticationToken(
				user, 
				null, 
				user.getAuthorities()
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
