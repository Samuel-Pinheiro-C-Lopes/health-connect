package io.github.samuel_pinheiro_c_lopes.spring_common.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "io.github.samuel_pinheiro_c_lopes.spring_common")
public class CommonFeignConfiguration {
}