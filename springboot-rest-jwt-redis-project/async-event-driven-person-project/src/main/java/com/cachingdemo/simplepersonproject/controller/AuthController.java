package com.cachingdemo.simplepersonproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cachingdemo.simplepersonproject.dto.AuthRequest;
import com.cachingdemo.simplepersonproject.dto.AuthResponse;
import com.cachingdemo.simplepersonproject.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

    	try {
    		Authentication auth = authManager.authenticate(
    				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    				);
    	} catch(UsernameNotFoundException e) {
    		logger.error("Incorrect Username or Password");
    		throw new UsernameNotFoundException(e.getMessage());
    	} catch(AuthenticationException e) {
    		logger.error("Incorrect Username or Password");
    		throw new BadCredentialsException("Credentials are incorrect");
    	}
    	String token = jwtUtil.generateToken(request.getUsername());
    	return ResponseEntity.ok(new AuthResponse(token));
    }
}

