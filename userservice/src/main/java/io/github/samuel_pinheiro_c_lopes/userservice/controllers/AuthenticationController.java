package io.github.samuel_pinheiro_c_lopes.userservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.samuel_pinheiro_c_lopes.spring_common.security.services.JWTService;
import io.github.samuel_pinheiro_c_lopes.userservice.models.User;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;
	private final JWTService jwtokenService;
	
	public AuthenticationController(final AuthenticationManager authenticationManager, final JWTService jwtokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtokenService = jwtokenService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<JWTResponseDTO> login(@RequestBody final AuthenticationRequestDTO authenticationRequest) {
			var authenticationToken = new UsernamePasswordAuthenticationToken(
					authenticationRequest.username(), 
					authenticationRequest.password()
			);
			
			try {
				var authorization = authenticationManager.authenticate(authenticationToken);
				var user = (User)authorization.getPrincipal();
				var jwtToken = this.jwtokenService.getToken(user);
				return ResponseEntity.ok(new JWTResponseDTO(jwtToken, user.getRoles().stream().map(r -> r.getAuthority()).toList()));			
			} catch (Exception ex) {
				throw ex;
			}
	}
	
	private record AuthenticationRequestDTO(String username, String password) { }
	private record JWTResponseDTO(String token, List<String> roles) { }
}
