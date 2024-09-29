package com.artcart.config;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {
//    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//
//    private String jwtSecret = "yourSecretKey";
    private static final String SECRET = "jlkfdsdfalkdafjlajfkldjsfadlkjfdlkafjdaksfldkfldfdsakjfdafhdifohdfdsfhdsklfjdslffdsjfldfdfljfdsflkjfdsfdfjslkfj"; // At least 32 characters
    private SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)); // Ensure the key meets HS256 requirements
    private int jwtExpirationMs = 24 * 60 * 60 * 1000; // 1 day in milliseconds
    private int refreshExpirationMs = 7 * 24 * 60 * 60 * 1000; // 1 week in milliseconds

    public String getEmailFromToken(String jwtToken) throws Exception{
        jwtToken = jwtToken.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }
    // Generate Refresh Token
    public String generateRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + refreshExpirationMs))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }
    // Validate Token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }

    // Get Username from Token
    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


    // Generate Access Token with roles
    public String generateAccessToken(Authentication authentication) {
        // Extract user roles from the authentication object
//        Set<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet());

        String roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet()).iterator().next();


        // Create the JWT token with roles as a claim
        String jwt = Jwts.builder()
                .setIssuer("your-app-name")   // Set the issuer
                .setSubject(authentication.getName())  // Set the subject (usually username/email)
                .setIssuedAt(new Date())  // Issue time
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))  // Expiration time
                .claim("email",authentication.getName())
                .claim("roles", roles)    // Add roles as a claim
                .signWith(key,SignatureAlgorithm.HS256)  // Sign the JWT with the secret key
                .compact();

        return jwt;
    }

    // Extract roles from Access Token
    public String getRolesFromToken(String jwtToken) {
        // Remove the "Bearer " prefix if present
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)  // Use the secret key to parse the token
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        String roles = String.valueOf(claims.get("roles"));

        return roles;
    }


}
