package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.exception.SignInException;
import com.artcart.model.SingInAndSingUp;
import com.artcart.request.SignInRequest;
import com.artcart.request.SignUpRequest;
import com.artcart.response.JwtResponse;
import com.artcart.response.SignUpResponse;
import com.artcart.services.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final  SingInAndSingUpService singInAndSingUpService;
    private final CustomUserService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final CustomerService customerService;
    private final SellerService sellerService;
    private final AdminServices adminServices;

    @PostMapping("/signup")
    @Operation(summary = "for signup",description = "for seller role= SELLER ,  for customer role = CUSTOMER , for admin role= ADMIN")
    public ResponseEntity<SignUpResponse> signUphandler(@RequestBody SignUpRequest signUpRequest){

        SingInAndSingUp singInAndSingUp = singInAndSingUpService.signUp(signUpRequest);
        if(singInAndSingUp == null){
            return new ResponseEntity<>(new SignUpResponse("user already Exits with given email",false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new SignUpResponse("Sign up successfully",true),HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    @Operation(summary = "for signin ",description = "return a jwt token")
    public ResponseEntity<JwtResponse> singInhanlder(@RequestBody SignInRequest signInRequest, HttpServletResponse response){
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        Authentication authentication = authenticate(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate access and refresh tokens
        String accessToken = tokenProvider.generateAccessToken(authentication); // Will be stored in local storage
        String refreshToken = tokenProvider.generateRefreshToken(authentication); // Will be stored in HTTP-only cookie
        // Set refresh token in HTTP-only, Secure cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)      // Cookie cannot be accessed via JavaScript
                .secure(true)        // Cookie will only be sent over HTTPS
                .path("/auth/refresh-token")  // Accessible only on the refresh token endpoint
//                .maxAge(7 * 24 * 60 * 60)  // Set expiration to 7 days
                .maxAge(60000)
//                .sameSite("Strict")  // Protect against CSRF attacks
                .build();

        // Add cookie to the response
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        String rolesFromToken = tokenProvider.getRolesFromToken(accessToken);
        // Return access token in response body
        return new ResponseEntity<>(new JwtResponse(accessToken,rolesFromToken),HttpStatus.OK);
    }


    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        // Validate the refresh token

        log.info(String.format("refresh token = %s",refreshToken));
        if (tokenProvider.validateJwtToken(refreshToken)) {
            // Extract user details from refresh token
            String username = tokenProvider.getUserNameFromToken(refreshToken);
            Authentication authentication = getAuthenticationForUser(username);
            // Generate new access token
            String newAccessToken = tokenProvider.generateAccessToken(authentication);
            // Return the new access token in response body (to be stored in local storage)
            String rolesFromToken = tokenProvider.getRolesFromToken(newAccessToken);
            return  new ResponseEntity<>(new JwtResponse(newAccessToken,rolesFromToken),HttpStatus.OK);
//            return ResponseEntity.ok(new JwtResponse(newAccessToken));
        } else {
            return new ResponseEntity<>("Invalid refresh token",HttpStatus.UNAUTHORIZED);
        }
    }
    public Authentication authenticate(String username,String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if(userDetails == null){
            throw new SignInException("No user found with this email");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new SignInException("password wrong");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    public Authentication getAuthenticationForUser(String username) {
        // Load user details by username
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        // Create an Authentication object based on the UserDetails
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // No credentials because this is after authentication
                userDetails.getAuthorities() // Pass user roles/authorities
        );
    }
}
