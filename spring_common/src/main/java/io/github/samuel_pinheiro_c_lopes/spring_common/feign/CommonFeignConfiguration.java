package io.github.samuel_pinheiro_c_lopes.spring_common.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import feign.RequestInterceptor;
import io.github.samuel_pinheiro_c_lopes.spring_common.security.interceptors.FeignClientInterceptor;
import io.github.samuel_pinheiro_c_lopes.spring_common.security.services.JWTService;

@Configuration
@EnableFeignClients(basePackages = "io.github.samuel_pinheiro_c_lopes.spring_common")
public class CommonFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(JWTService jwtService) {
        return new FeignClientInterceptor(jwtService);
    }
}