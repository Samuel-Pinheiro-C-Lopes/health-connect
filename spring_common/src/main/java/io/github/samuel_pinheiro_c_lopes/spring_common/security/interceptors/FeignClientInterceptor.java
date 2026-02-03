package io.github.samuel_pinheiro_c_lopes.spring_common.security.interceptors;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.samuel_pinheiro_c_lopes.spring_common.security.services.JWTService;
import jakarta.servlet.http.HttpServletRequest;

public class FeignClientInterceptor implements RequestInterceptor {
    
    // We don't strictly need JWTService if we just copy the string, 
    // but we use it for the HEADER constant.
    private final JWTService jwtservice;
    
    public FeignClientInterceptor(final JWTService jwtService) {    
        this.jwtservice = jwtService;
    }
    
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // 1. Get the raw header (e.g., "Bearer eyJ...")
            String tokenHeader = request.getHeader(jwtservice.AUTHORIZATION_HEADER);
            
            // 2. Simply check if it exists
            if (tokenHeader != null && !tokenHeader.isBlank()) {
                // 3. Propagate it exactly as is. 
                // Do NOT re-validate here; let the destination service validate it.
                template.header(jwtservice.AUTHORIZATION_HEADER, tokenHeader);
            }
        }
    }
}