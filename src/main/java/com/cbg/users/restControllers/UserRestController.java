package com.cbg.users.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.cbg.users.entities.User;
import com.cbg.users.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

	@Autowired
	UserService userService ;
	
	@GetMapping("all")
	public List<User>  getAllUsers(){
		return userService.findAllUsers();
	}
	
}
