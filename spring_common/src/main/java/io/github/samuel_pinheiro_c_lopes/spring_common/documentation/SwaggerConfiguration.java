package io.github.samuel_pinheiro_c_lopes.spring_common.documentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
	@Bean
	public OpenAPI openAPI(@Value("${spring.application.name:Unknown Service}") String appName) {
		return new OpenAPI()
				.addSecurityItem(
					new SecurityRequirement().addList("Bearer Authentication")
				)
				.components(
					new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
				)
				.info(
					new Info()
						.title(appName)
				);
	}
	
	private SecurityScheme createAPIKeyScheme() {
	    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
	        .bearerFormat("JWT")
	        .scheme("bearer");
	}
}