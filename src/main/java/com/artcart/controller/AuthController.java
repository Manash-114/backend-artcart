package com.artcart.controller;

import com.artcart.config.JwtTokenProvider;
import com.artcart.exception.SignInException;
import com.artcart.model.SingInAndSingUp;
import com.artcart.request.SignInRequest;
import com.artcart.request.SignUpRequest;
import com.artcart.response.AuthResponse;
import com.artcart.response.SignUpResponse;
import com.artcart.services.CustomUserService;
import com.artcart.services.SingInAndSingUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private SingInAndSingUpService singInAndSingUpService;
    private CustomUserService customUserService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    public AuthController(SingInAndSingUpService singInAndSingUpService,CustomUserService customUserService,PasswordEncoder passwordEncoder,JwtTokenProvider tokenProvider){
        this.singInAndSingUpService  = singInAndSingUpService;
        this.passwordEncoder = passwordEncoder;
        this.customUserService  = customUserService;
        this.tokenProvider = tokenProvider;
    }
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUphandler(@RequestBody SignUpRequest signUpRequest){

        SingInAndSingUp singInAndSingUp = singInAndSingUpService.signUp(signUpRequest);
        if(singInAndSingUp == null){
            return new ResponseEntity<>(new SignUpResponse("user already Exits with given email",false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new SignUpResponse("Sign up successfully",true),HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> singInhanlder(@RequestBody SignInRequest signInRequest){
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        Authentication authentication = authenticate(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse(token,true), HttpStatus.ACCEPTED);
    }

    public Authentication authenticate(String username,String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if(userDetails == null){
            throw new SignInException("invalid username");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new SignInException("invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


}
