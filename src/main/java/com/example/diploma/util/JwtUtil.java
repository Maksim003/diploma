package com.example.diploma.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {

    public static String generateToken(String username, Long expirationMs, SecretKey secretKey) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public static String extractUsername(String token, SecretKey secretKey) throws MalformedJwtException, SignatureException {
        return extractClaim(token, secretKey, Claims::getSubject);
    }

    public static Date extractExpiration(String token, SecretKey secretKey) throws MalformedJwtException, SignatureException {
        return extractClaim(token, secretKey, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, SecretKey secretKey, Function<Claims, T> claimsResolver) throws MalformedJwtException, SignatureException {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    public static Boolean isTokenExpired(String token, SecretKey secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    private static Claims extractAllClaims(String token, SecretKey secretKey) throws MalformedJwtException, SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
