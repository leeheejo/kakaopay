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

import com.kakaopay.constant.Constant;
import com.kakaopay.exception.InvalidUserException;
import com.kakaopay.model.User;
import com.kakaopay.request.RequestUserDefault;
import com.kakaopay.response.ReturnDefault;
import com.kakaopay.response.TokenDTO;
import com.kakaopay.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping(value = "/user/signup")
	public @ResponseBody ResponseEntity<Object> signUp(final @Valid @RequestBody RequestUserDefault user)
			throws InvalidUserException, UnsupportedEncodingException {
		
		User userInfo = new User(user.getUserId(), user.getPassword());
		String token = userService.signUpUser(userInfo);
		
		ReturnDefault msg = ReturnDefault.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).result(new TokenDTO(token)).build();
		
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@PostMapping(value = "/user/signin")
	public @ResponseBody ResponseEntity<Object> signIn(final @Valid @RequestBody RequestUserDefault user)
			throws InvalidUserException, UnsupportedEncodingException {
		
		User userInfo = new User(user.getUserId(), user.getPassword());
		// 패스워드 검사
		String token = userService.signInUser(userInfo);
		
		ReturnDefault msg = ReturnDefault.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).result(new TokenDTO(token)).build();
		
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
}
