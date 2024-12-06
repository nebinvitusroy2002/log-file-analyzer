package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.exceptions.FileProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        try{
            log.info("Extracting username from token.");
            return extractClaim(token, Claims::getSubject);
        }catch (JwtException | IllegalArgumentException e){
            log.error("Error extracting username from token: {}",e.getMessage(),e);
            throw new FileProcessingException("Invalid token: Unable to extract username.",e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error extracting claim from token: {}",e.getMessage(),e);
            throw new FileProcessingException("Invalid token: Unable to extract claim.",e);
        }
    }
    public String generateToken(UserDetails userDetails) {
        try{
            log.info("Generating token for user: {}",userDetails.getUsername());
            return generateToken(new HashMap<>(), userDetails);
        }catch (JwtException e){
            log.error("Error generating token: {}",e.getMessage(),e);
            throw new FileProcessingException("Token generation failed.",e);
        }
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try{
            log.info("Generating token with additional claims for user: {}",userDetails.getUsername());
            return buildToken(extraClaims, userDetails, jwtExpiration);
        }catch (JwtException e){
            log.error("Error generating token with claims: {}",e.getMessage(),e);
            throw new FileProcessingException("Token generation with claims failed.",e);
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (JwtException e){
            log.error("Error building token: {}",e.getMessage(),e);
            throw new FileProcessingException("Error building token",e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            log.info("Token validation result: {}", isValid);
            return isValid;
        }catch (JwtException | IllegalArgumentException e){
            log.error("Error validating token: {}", e.getMessage(), e);
            throw new FileProcessingException("Invalid token.", e);
        }
    }

    private boolean isTokenExpired(String token) {
        try{
            return extractExpiration(token).before(new Date());
        }catch (JwtException e){
            log.error("Error checking token expiration: {}", e.getMessage(), e);
            throw new FileProcessingException("Unable to check token expiration.", e);
        }
    }

    private Date extractExpiration(String token) {
        try{
            return extractClaim(token, Claims::getExpiration);
        }catch (JwtException | IllegalArgumentException e){
            log.error("Error extracting expiration date from token: {}", e.getMessage(), e);
            throw new FileProcessingException("Unable to extract token expiration date.", e);
        }
    }

    private Claims extractAllClaims(String token) {
        try{
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            log.error("Error extracting claims from token: {}", e.getMessage(), e);
            throw new FileProcessingException("Invalid token: Unable to extract claims.", e);
        }
    }

    private Key getSignInKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }catch (Exception e){
            log.error("Error decoding the secret key: {}", e.getMessage(), e);
            throw new FileProcessingException("Secret key decoding failed.", e);
        }
    }
}
