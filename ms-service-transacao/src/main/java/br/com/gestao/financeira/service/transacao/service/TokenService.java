package br.com.gestao.financeira.service.transacao.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "API Gestao Financeira";
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";

    public String getSubject(String tokenJWT) {
        try {
            return decode(tokenJWT).getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado.", exception);
        }
    }

    public List<String> getRoles(String tokenJWT) {
        try {
            return decode(tokenJWT)
                    .getClaim(CLAIM_ROLES)
                    .asList(String.class);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado.", exception);
        }
    }

    public Long getUserId(String tokenJWT) {
        try {
            return decode(tokenJWT)
                    .getClaim(CLAIM_USER_ID)
                    .asLong();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado.", exception);
        }
    }

    private DecodedJWT decode(String tokenJWT) {
        var algoritmo = Algorithm.HMAC256(secret);
        return jwtVerifier(algoritmo).verify(tokenJWT);
    }

    private JWTVerifier jwtVerifier(Algorithm algoritmo) {
        return JWT.require(algoritmo)
                .withIssuer(ISSUER)
                .build();
    }
}