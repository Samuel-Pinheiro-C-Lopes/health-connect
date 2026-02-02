package io.github.samuel_pinheiro_c_lopes.spring_common.security.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import io.github.samuel_pinheiro_c_lopes.spring_common.security.interceptors.FeignClientInterceptor;
import io.github.samuel_pinheiro_c_lopes.spring_common.security.services.JWTService;

@Configuration
public class FeignClientInterceptorConfiguration {
	@Bean
	public RequestInterceptor requestInterceptor(final JWTService jwtService) {
        return new FeignClientInterceptor(jwtService);
    }
}
