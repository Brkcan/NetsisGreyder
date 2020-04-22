package com.netsis.user;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.netsis.shared.GenericResponse;


@RestController
@CrossOrigin
public class UserController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/api/1.0/users")
	public GenericResponse createUser(@RequestBody User user) {
		log.info(user.toString());
		userService.save(user);
		GenericResponse response = new GenericResponse();
		response.setMessage("user Create");
		return response;
	}
}
