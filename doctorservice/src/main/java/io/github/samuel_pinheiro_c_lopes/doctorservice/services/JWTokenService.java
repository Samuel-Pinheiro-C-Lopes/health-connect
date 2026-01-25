package io.github.samuel_pinheiro_c_lopes.doctorservice.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTokenService {
    private final Algorithm algorithm;
    private final String issuer;

    public JWTokenService(
            @Value("${app.authentication.token.secret}") final String secret,
            @Value("${app.authentication.token.issuer}") final String issuer
    ) {
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String getSubject(final String jwt) {
        try {
            return JWT.require(algorithm).withIssuer(this.issuer).build().verify(jwt).getSubject();
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Error when trying to verify JWT token: " + ex.getMessage());
        }
    }

    public List<String> getRoles(final String jwt) {
        try {
            final DecodedJWT decodedJwt = JWT.require(algorithm).withIssuer(this.issuer).build().verify(jwt);

            if (decodedJwt.getClaim("roles").isNull()) {
                return Collections.emptyList();
            }

            return decodedJwt.getClaim("roles").asList(String.class);
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Error when trying to verify JWT token: " + ex.getMessage());
        }
    }
}
