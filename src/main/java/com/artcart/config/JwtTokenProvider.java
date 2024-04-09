package com.artcart.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public  String generateToken(Authentication authentication){
        String role = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet()).iterator().next();
                String jwt = Jwts.builder().setIssuer("manash")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",authentication.getName())
                .claim("role",role)
                .signWith(key)
                .compact();
        return  jwt;
    }

    public String getEmailFromToken(String jwtToken) throws Exception{
        jwtToken = jwtToken.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }

    public String getRoleFromToken(String jwtToken){
        jwtToken = jwtToken.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        String roles = String.valueOf(claims.get("role"));
        System.out.println(roles);
        return roles;
    }
}
