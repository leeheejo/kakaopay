package com.kakaopay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@PostMapping(value = "/user/signup")
	public @ResponseBody ResponseEntity<Object> signUp(@RequestHeader HttpHeaders headers) {
		String pass = "123";
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
