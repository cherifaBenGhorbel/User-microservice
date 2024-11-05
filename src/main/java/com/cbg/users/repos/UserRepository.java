package com.cbg.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cbg.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
