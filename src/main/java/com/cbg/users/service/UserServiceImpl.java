package com.cbg.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbg.users.entities.Role;
import com.cbg.users.entities.User;
import com.cbg.users.repos.RoleRepository;
import com.cbg.users.repos.UserRepository;
import com.cbg.users.service.exceptions.EmailAlreadyExistsException;
import com.cbg.users.service.register.RegistrationRequest;
import com.cbg.users.service.register.VerificationToken;
import com.cbg.users.service.register.VerificationTokenRepository;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRep;

	@Autowired
	RoleRepository roleRep;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepo;

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

	@Override
	public User registerUser(RegistrationRequest request) {
		Optional<User> optionaluser = userRep.findByEmail(request.getEmail());
		if(optionaluser.isPresent())
			throw new EmailAlreadyExistsException("email already exist !!!");
		User newUser = new User();
		newUser.setUsername(request.getUsername());
		newUser.setEmail(request.getEmail());
		
		newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
		newUser.setEnabled(false);
		
		userRep.save(newUser);
		
		//add newUser the role par default USER
		Role r = roleRep.findByRole("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(r);
		newUser.setRoles(roles);
		
		//genere the secret code 
		String code = this.generateCode();
		
		VerificationToken token = new VerificationToken(code, newUser);
		verificationTokenRepo.save(token);

		 return userRep.save(newUser);
	}

	public String generateCode() {
		 Random random = new Random();
		 Integer code = 100000 + random.nextInt(900000);

		 return code.toString();
		}
}
