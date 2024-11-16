package com.cbg.users.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cbg.users.entities.User;
import com.cbg.users.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userService.findUserByUsername(username);

		if (user == null)
			throw new UsernameNotFoundException("User Not Found");

		List<GrantedAuthority> auths = new ArrayList<>();

		user.getRoles().forEach(role -> {
			GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
			auths.add(authority);
		});

		return new org.springframework.security.core.
				userdetails.User(user.getUsername(), 
						user.getPassword(),
						user.getEnabled(),
						true,
						true,
						true, 
						auths
						);
	}

}
