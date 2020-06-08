package com.kakaopay.controller;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.exeption.InvalidUserException;
import com.kakaopay.model.User;
import com.kakaopay.request.RequestUserDefault;
import com.kakaopay.response.ReturnToken;
import com.kakaopay.service.UserService;
import com.kakaopay.utils.SHA256Util;

@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@PostMapping(value = "/user/signup")
	public @ResponseBody ResponseEntity<Object> signUp(@RequestHeader HttpHeaders headers,
			final @Valid @RequestBody RequestUserDefault user)
			throws InvalidUserException, UnsupportedEncodingException {
		logger.info(user.toString());
		User userInfo = new User(user.getUserId(), user.getPassword());
		String token = userService.signUpUser(userInfo);
		ReturnToken returnToken = new ReturnToken(token);
		return new ResponseEntity<>(returnToken, HttpStatus.OK);
	}

	@PostMapping(value = "/user/signin")
	public @ResponseBody ResponseEntity<Object> signIn(@RequestHeader HttpHeaders headers,
			final @Valid @RequestBody RequestUserDefault user)
			throws InvalidUserException, UnsupportedEncodingException {
		User userInfo = new User(user.getUserId(), user.getPassword());
		// 패스워드 검사
		String token = userService.signInUser(userInfo);
		ReturnToken returnToken = new ReturnToken(token);
		return new ResponseEntity<>(returnToken, HttpStatus.OK);
	}
}
