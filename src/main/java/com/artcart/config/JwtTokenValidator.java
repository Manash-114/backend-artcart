package com.artcart.config;

import com.artcart.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class JwtTokenValidator extends OncePerRequestFilter {

    //this is very important
    private HandlerExceptionResolver handlerExceptionResolver;

    private static final String SECRET = "jlkfdsdfalkdafjlajfkldjsfadlkjfdlkafjdaksfldkfldfdsakjfdafhdifohdfdsfhdsklfjdslffdsjfldfdfljfdsflkjfdsfdfjslkfj"; // At least 32 characters
    private SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)); // Ensure the key meets HS256 requirements
    public JwtTokenValidator(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        try{
            if(jwt != null){
                //Bearer + token
                jwt = jwt.substring(7);
//                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                String userName = String.valueOf(claims.get("email"));
//                String authorities =  String.valueOf(claims.get("authorities"));
                String authorities =  String.valueOf(claims.get("roles"));
                log.info(String.format("email = %s and roles = %s",userName,authorities));
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication =  new UsernamePasswordAuthenticationToken(userName,null,auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request,response);
        }catch (Exception e){
            e.printStackTrace();
            handlerExceptionResolver.resolveException(request,response,null,e);

        }

    }
}
