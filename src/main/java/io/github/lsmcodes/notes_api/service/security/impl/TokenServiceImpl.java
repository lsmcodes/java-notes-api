package io.github.lsmcodes.notes_api.service.security.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.security.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Implements {@link TokenService} interface methods.
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${security.token.key}")
    public String key;

    @Value("${security.token.expiration}")
    public Long expiration;

    /**
     * {@inheritDoc}
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .claims()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(generateExpirationDate())
                .and()
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    /**
     * {@inheritDoc}
     */
    public String getSubjectFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(key.getBytes())).build().parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }

    /**
     * {@inheritDoc}
     */
    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

}