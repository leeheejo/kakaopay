package com.kakaopay.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.kakaopay.exeption.InvalidUserException;
import com.kakaopay.model.User;
import com.kakaopay.repo.UserRepository;
import com.kakaopay.response.TokenDTO;
import com.kakaopay.service.UserService;
import com.kakaopay.utils.JWTUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
	private static boolean setUpFlag = false;

	@Autowired
	UserRepository userRepo;
	@Autowired
	UserService userService;
	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void init() throws UnsupportedEncodingException, InvalidUserException {
		if (setUpFlag) {
			return;
		}

		String id = "testId123";
		String pass = "pass123";
		userService.signUpUser(new User(id, pass));
	}

	@Test
	public void signUp() throws UnsupportedEncodingException {

		String id = "testId567";
		String pass = "pass567";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		Map<String, String> map = new HashMap<>();
		map.put("userId", id);
		map.put("password", pass);
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<TokenDTO> response = restTemplate.postForEntity("/user/signup", request, TokenDTO.class);
		System.out.println(response.getStatusCode());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(JWTUtils.validateToken(response.getBody().getToken())).isEqualTo(true);
		assertThat(userRepo.findAll().size()).isEqualTo(2);
	}

	@Test
	public void signIn() throws UnsupportedEncodingException {

		String id = "testId123";
		String pass = "pass123";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		Map<String, String> map = new HashMap<>();
		map.put("userId", id);
		map.put("password", pass);
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<TokenDTO> response = restTemplate.postForEntity("/user/signin", request, TokenDTO.class);
		System.out.println(response.getStatusCode());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(JWTUtils.validateToken(response.getBody().getToken())).isEqualTo(true);
	}
}
