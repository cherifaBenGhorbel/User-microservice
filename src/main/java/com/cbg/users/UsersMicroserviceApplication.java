package com.cbg.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cbg.users.entities.Role;
import com.cbg.users.entities.User;
import com.cbg.users.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UsersMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersMicroserviceApplication.class, args);
	}

	@Autowired
	UserService userService;

/*	
	  @PostConstruct
	void init_users() {

		// add roles
		userService.addRole(new Role(null, "ADMIN"));
		userService.addRole(new Role(null, "USER"));

		// add users
		userService.saveUser(new User(null, "admin", "123", true, null,null));
		//userService.saveUser(new User(null, "cherifa", "123", true, null));
		//userService.saveUser(new User(null, "ines", "123", true, null));

		// add roles to users
		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("admin", "USER");
		//userService.addRoleToUser("cherifa", "USER");
		//userService.addRoleToUser("ines", "USER");
	}
*/

}
