package com.cbg.users.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.cbg.users.entities.User;
import com.cbg.users.service.UserService;
import com.cbg.users.service.register.RegistrationRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

	@Autowired
	UserService userService ;
	
	@GetMapping("all")
	public List<User>  getAllUsers(){
		return userService.findAllUsers();
	}
	
	@PostMapping("/register")
	public User register(@RequestBody RegistrationRequest request){
		return userService.registerUser(request);
	}

	@GetMapping("/verifyEmail/{token}")
	 public User verifyEmail(@PathVariable("token") String token){
			return userService.validateToken(token);
	 }

	
}
