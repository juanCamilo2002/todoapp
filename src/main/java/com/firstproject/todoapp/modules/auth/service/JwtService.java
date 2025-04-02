package com.firstproject.todoapp.modules.auth.service;

import com.firstproject.todoapp.shared.exceptions.ExpiredTokenException;
import com.firstproject.todoapp.shared.exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private  String SECRET_KEY;

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try{
            return extractClaim(token, Claims::getSubject);
        }catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Expired Token");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid Token");
        }
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("Token expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Token invalid or malformed");
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Token invalid or malformed");
        }
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .claim("token_type", "refresh_token")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isRefreshTokenValid(String token, String username){
        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("token_type", String.class);
            return extractUsername(token).equals(username)
                    && !isTokenExpired(token)
                    && "refresh_token".equals(tokenType);
        }catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("Refresh Token expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid Refresh Token");
        }
    }
}
