package com.artcart.exception;


import com.artcart.response.InvalidDetailResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorDetails> userNotFoundHandler(UserNotFound exception , WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignInException.class)
    public ResponseEntity<InvalidDetailResponse> userNotFoundHandler(SignInException exception , WebRequest request){
        InvalidDetailResponse invalidDetailResponse = new InvalidDetailResponse(exception.getMessage());
        return new ResponseEntity<>(invalidDetailResponse, HttpStatus.OK);
    }

    @ExceptionHandler(CloudinaryImageUploadException.class)
    public ResponseEntity<ErrorDetails> cloudinaryImageUploadException(CloudinaryImageUploadException exception , WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail invalidTokenHandler(Exception e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,e.getMessage());
        errorDetails.setProperty("message","invalid-jwt-Token");
        return  errorDetails;
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ProblemDetail malformedJwtExceptionHandler(Exception e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,e.getMessage());
        errorDetails.setProperty("message","invalid-Token");
        return  errorDetails;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail expiredJwtExceptionHandler(Exception e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,e.getMessage());
        errorDetails.setProperty("message","jwt-token-expired");
        return  errorDetails;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accessDeniedException(AccessDeniedException e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,e.getMessage());
        errorDetails.setProperty("message","Access-Denied");
        return  errorDetails;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail resourceNotFoundHandler(ResourceNotFoundException e){
        ProblemDetail errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,e.getMessage());
        errorDetails.setProperty("message",e.getMessage());
        return  errorDetails;
    }
}
