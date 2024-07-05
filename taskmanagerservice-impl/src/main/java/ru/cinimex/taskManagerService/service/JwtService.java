package ru.cinimex.taskManagerService.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration durationLifeTime;

    public String generateToken(Authentication authenticate) {

        if (authenticate.getPrincipal() instanceof User user){
            Map<String, Object> claims = new HashMap<>();

            claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

            Date issuedDate = new Date();
            Date expirationDate = new Date(issuedDate.getTime() + durationLifeTime.toMillis());

            return Jwts.builder()
                    .claims(claims)
                    .subject(user.getUsername())
                    .issuedAt(issuedDate)
                    .expiration(expirationDate)
                    .signWith(getSecretKey(), Jwts.SIG.HS256)
                    .compact();
        }
        throw new IllegalArgumentException("Incorrect type of authentication principal");
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String executeUserName(String token){
        return exctractClaim(token, Claims::getSubject);
    }

    public Date executeExpirationDate(String token){
        return exctractClaim(token, Claims::getExpiration);
    }


    private <T> T exctractClaim(String token, Function<Claims, T> claimsExtractor){
        final Claims claims = extractClaimsFromToken(token);
        return claimsExtractor.apply(claims);
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token){
        return executeExpirationDate(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = executeUserName(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
