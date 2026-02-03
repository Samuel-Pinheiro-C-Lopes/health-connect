package io.github.samuel_pinheiro_c_lopes.userservice.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.github.samuel_pinheiro_c_lopes.spring_common.security.filters.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    
    private final JWTAuthenticationFilter jwtAuthenticationToken;
    
    @Autowired
    public SecurityConfiguration(final JWTAuthenticationFilter jwtAuthenticationToken) {
        this.jwtAuthenticationToken = jwtAuthenticationToken;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(basic -> basic.disable())
                .formLogin(login -> login.disable())
                .csrf(csrf -> csrf.disable())
                
                // 1. DISABLE CORS HERE (Critical for Gateway integration)
                .cors(cors -> cors.disable()) 
                
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    // 2. FIX PATTERNS: Remove "/**/..."
                    // Only use the exact path the controller listens to.
                    req.requestMatchers(HttpMethod.POST, "/authentication/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/user").permitAll();
                    
                    req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
    // 3. DELETE the corsConfigurationSource Bean completely!
    // Having it here causes the "Multiple CORS header" error in your browser screenshot.
}