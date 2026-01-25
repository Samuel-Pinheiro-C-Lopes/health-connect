package io.github.samuel_pinheiro_c_lopes.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.userservice.models.User;
import io.github.samuel_pinheiro_c_lopes.userservice.services.JWTokenService;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;
	private final JWTokenService jwtokenService;
	
	public AuthenticationController(final AuthenticationManager authenticationManager, final JWTokenService jwtokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtokenService = jwtokenService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<JWTResponseDTO> login(@RequestBody final AuthenticationRequestDTO authenticationRequest) {
			var authenticationToken = new UsernamePasswordAuthenticationToken(
					authenticationRequest.username(), 
					authenticationRequest.password()
			);
			
			var authorization = authenticationManager.authenticate(authenticationToken);
			
			var jwtToken = this.jwtokenService.getToken((User)authorization.getPrincipal());
			
			return ResponseEntity.ok(new JWTResponseDTO(jwtToken));			
	}
	
	private record AuthenticationRequestDTO(String username, String password) { }
	private record JWTResponseDTO(String token) { }
}
