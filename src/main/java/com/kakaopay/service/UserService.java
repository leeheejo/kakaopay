package com.kakaopay.service;

import java.io.UnsupportedEncodingException;

import com.kakaopay.exception.InvalidUserException;
import com.kakaopay.model.User;
import com.kakaopay.request.RequestUserDefault;

public interface UserService {
	public String signUpUser(User userInfo) throws InvalidUserException, UnsupportedEncodingException;

	public String signInUser(User userInfo) throws InvalidUserException, UnsupportedEncodingException;
}
