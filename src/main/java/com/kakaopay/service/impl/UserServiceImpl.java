package com.kakaopay.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.exeption.InvalidUserException;
import com.kakaopay.model.User;
import com.kakaopay.repo.UserRepository;
import com.kakaopay.service.UserService;
import com.kakaopay.utils.JWTUtils;
import com.kakaopay.utils.SHA256Util;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Override
	public String signUpUser(User userInfo) throws InvalidUserException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String encryptedPass = SHA256Util.encrypt(userInfo.getPassword());
		User user = new User(userInfo.getUserId(), encryptedPass);
		if (userRepo.findOneByUserId(user.getUserId()) != null)
			throw new InvalidUserException("UserId already Used");

		user = userRepo.save(user);

		return JWTUtils.generateToken(user.getUserId());
	}

	@Override
	public String signInUser(User userInfo) throws InvalidUserException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		User user = userRepo.findOneByUserId(userInfo.getUserId());
		if (user == null)
			throw new InvalidUserException("UserId is not exist");

		String encryptedPass = SHA256Util.encrypt(userInfo.getPassword());
		if (!encryptedPass.equals(user.getPassword()))
			throw new InvalidUserException("Password is incorrect");

		return JWTUtils.generateToken(user.getUserId());
	}

}