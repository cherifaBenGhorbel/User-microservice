package com.cbg.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbg.users.entities.Role;
import com.cbg.users.entities.User;
import com.cbg.users.repos.RoleRepository;
import com.cbg.users.repos.UserRepository;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRep;

	@Autowired
	RoleRepository roleRep;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}

	@Override
	public Role addRole(Role role) {
		return roleRep.save(role);
	}

	@Override
	public User addRoleToUser(String username, String rolename) {
	    User usr = userRep.findByUsername(username);
	    Role r = roleRep.findByRole(rolename);
	    usr.getRoles().add(r);
	    return userRep.save(usr);  // Save the modified user
	}
	
	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}


}
