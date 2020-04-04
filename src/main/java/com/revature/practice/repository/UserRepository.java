package com.revature.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.revature.practice.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUserName(String userName);
}
