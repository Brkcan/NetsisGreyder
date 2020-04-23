package com.netsis.user;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.netsis.error.ApiError;
import com.netsis.shared.GenericResponse;


@RestController
@CrossOrigin
public class UserController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/api/1.0/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		log.info(user.toString());
		
		userService.save(user);
		GenericResponse response = new GenericResponse();
		response.setMessage("user Create");
		return ResponseEntity.ok(response);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerValidationException(MethodArgumentNotValidException exception) {
		ApiError apiError = new ApiError(400, "Validation Error", "/api/1.0/users");
		Map<String, String> validationError = new HashMap<String, String>();
		for(FieldError fieldError: exception.getBindingResult().getFieldErrors()) {
			validationError.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		apiError.setValidationErrors(validationError);
		return apiError;
	}
	
	
	
	
	
	
	
}
