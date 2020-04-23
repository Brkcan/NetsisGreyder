package com.netsis.auth;

import java.util.Base64;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.netsis.error.ApiError;
import com.netsis.user.User;
import com.netsis.user.UserController;
import com.netsis.user.UserRepository;

@RestController
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

	PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder(); 

	@PostMapping("/api/1.0/auth")
	public ResponseEntity<?> handlerAuthencation(@RequestHeader(name = "Authorization", required = false) String authorization) {
		if(authorization == null ) {
			ApiError apiError = new ApiError(401, "unAuthenzation", "/api/1.0/auth");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
		}
		
		String Base64Encoder = authorization.split("Basic ")[1];
		String decoded = new String(Base64.getDecoder().decode(Base64Encoder));
		String[] parts = decoded.split(":");
		String username = parts[0];
		String password = parts[1];
		
		User inDB = userRepository.findByUsername(username);
		if(inDB == null) {
			ApiError apiError = new ApiError(401, "unAuthenzation", "/api/1.0/auth");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
		}
		String hashedPassword = inDB.getPassword();
		if(!passwordEncoder.matches(password, hashedPassword)) {
			ApiError apiError = new ApiError(401, "unAuthenzation", "/api/1.0/auth");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
		}
		log.info(authorization);
		
		return ResponseEntity.ok().build();
	}
}
