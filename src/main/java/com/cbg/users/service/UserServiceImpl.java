package com.cbg.users.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.cbg.users.service.exceptions.ExpiredTokenException;
import com.cbg.users.service.exceptions.InvalidTokenException;
import com.cbg.users.service.register.RegistrationRequest;
import com.cbg.users.service.register.VerificationToken;
import com.cbg.users.service.register.VerificationTokenRepository;
import com.cbg.users.util.EmailSender;

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

	@Autowired
	EmailSender emailSender;

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

		//send the code by email to the user
		sendEmailUser(newUser, token.getToken());

		return userRep.save(newUser);
	}

	public String generateCode() {
		Random random = new Random();
		Integer code = 100000 + random.nextInt(900000);

		return code.toString();
	}
	
	@Override
	public void sendEmailUser(User u, String code) {
		String emailBody ="Hello "+ "<h1>"+u.getUsername() +"</h1>" +
				" This is your validation code "+"<h1>"+code+"</h1>";

		emailSender.sendEmail(u.getEmail(), emailBody);
	}

	@Override
	public User validateToken(String code) {
		VerificationToken token = verificationTokenRepo.findByToken(code);
		
		if(token == null){
			throw new InvalidTokenException("Invalid Token");
		}

		User user = token.getUser();
		
		Calendar calendar = Calendar.getInstance();
		
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
			verificationTokenRepo.delete(token);
			throw new ExpiredTokenException("expired Token");
		}
		
		user.setEnabled(true);
		userRep.save(user);
		return user;
	}


}
