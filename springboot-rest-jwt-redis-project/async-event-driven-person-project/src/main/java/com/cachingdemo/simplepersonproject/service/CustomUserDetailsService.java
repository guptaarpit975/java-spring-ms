package com.cachingdemo.simplepersonproject.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private List<String> allowedUsers = Arrays.asList("Romario", "Ronaldo", "Raul", "Maradona");
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // You can load user from DB here. For now, return a hardcoded user
    	
    	System.out.println("Trying to authenticate the user : " + username);
    	if(!allowedUsers.contains(username)) {
    		throw new UsernameNotFoundException("User: + " + username + " not found in the system.");
    	}
    	
    	logger.info("User - " + username + " is an authentic user. Validating password ...");
        return new User(
                username,
                new BCryptPasswordEncoder().encode("password"),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}

