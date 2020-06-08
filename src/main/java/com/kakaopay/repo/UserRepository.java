package com.kakaopay.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findOneByUserId(String userId);
}
