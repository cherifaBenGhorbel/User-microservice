package com.cbg.users.service;

import java.util.List;

import com.cbg.users.entities.Role;
import com.cbg.users.entities.User;
import com.cbg.users.service.register.RegistrationRequest;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername(String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User> findAllUsers();
	
	User registerUser( RegistrationRequest request);

}
