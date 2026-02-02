package io.github.samuel_pinheiro_c_lopes.spring_common.security.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.samuel_pinheiro_c_lopes.spring_common.security.services.JWTService;
import jakarta.servlet.http.HttpServletRequest;

public class FeignClientInterceptor implements RequestInterceptor{
	private final JWTService jwtservice;
	
	@Autowired
	public FeignClientInterceptor(final JWTService jwtService) {	
		this.jwtservice = jwtService;
	}
	
    @Override
    public void apply(RequestTemplate template) {
        // 1. Get the current incoming HTTP request (from the user/gateway)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // 2. Extract the "Authorization" header (Bearer ...)
            String token = request.getHeader(jwtservice.AUTHORIZATION_HEADER);
            
            // 3. Validates token
            if (!jwtservice.validateToken(token))
    			return;
            
            // 4. If a token exists, propagate it to the Feign request
            template.header(jwtservice.AUTHORIZATION_HEADER, token);
        }
        
    }
}