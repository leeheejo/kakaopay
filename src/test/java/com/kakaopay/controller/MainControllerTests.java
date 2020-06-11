package com.kakaopay.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.kakaopay.model.Coupon;
import com.kakaopay.repo.CouponRepository;
import com.kakaopay.response.CouponListDTO;
import com.kakaopay.response.ReturnDefault;
import com.kakaopay.service.CouponService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTests {
	private static boolean setUpFlag = false;
	@Autowired
	CouponService couponService;
	@Autowired
	CouponRepository couponRepo;
	@Autowired
	private TestRestTemplate restTemplate;

	// 7월 9일까지 테스트용 토큰 발급
	private static String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTQyNzQ5NjksInVzZXJJZCI6IlpFZFdlbVJGYkd0T1ZGa3oiLCJpYXQiOjE1OTE2ODI3OTMyMjB9.U2fBICMxqVXedM2BQwhFZ_jClGVBqAPg5z4YrYVvK4o";

	@Before
	public void init() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		if (setUpFlag) {
			return;
		}

		couponService.generateCoupon(5L);
		List<Coupon> testList = couponRepo.findAll();
		String couponNumForTest = couponService.issueCoupon(token);
		assertThat(couponNumForTest).isNotBlank();
		setUpFlag = true;
	}

	@Test
	public void generateCoupon() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Authorization", token);
		Map<String, String> map = new HashMap<>();
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("/coupon/10", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void issueCoupon() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Authorization", token);
		Map<String, String> map = new HashMap<>();
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<ReturnDefault> response = restTemplate.exchange("/coupon/issue", HttpMethod.PUT, request,
				ReturnDefault.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getClass()).isNotNull();
	}

	@Test
	public void findIssuedCoupon() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Authorization", token);
		Map<String, String> map = new HashMap<>();
		HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
		ResponseEntity<ReturnDefault> response = restTemplate.exchange("/coupon/issue", HttpMethod.GET, request,
				ReturnDefault.class);
		assertThat(response.getBody().getResult()).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getResult()).isNotNull();
	}

	@Test
	public void useCoupon() throws Exception {
		String couponNumForTest = couponService.issueCoupon(token);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		Map<String, String> map = new HashMap<>();
		map.put("coupon", couponNumForTest);
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange("/coupon/use", HttpMethod.PUT, request, String.class);
		System.out.println(response.getBody().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void cancelCoupon() throws Exception {
		String couponNumForTest = couponService.issueCoupon(token);
		couponService.changeUseCoupon(token, couponNumForTest, false);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Authorization", token);
		Map<String, String> map = new HashMap<>();
		map.put("coupon", couponNumForTest);
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange("/coupon/cancel", HttpMethod.PUT, request,
				String.class);
		System.out.println(response.getBody().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void findExpireTodayCoupon() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Authorization", token);
		Map<String, String> map = new HashMap<>();
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<ReturnDefault> response = restTemplate.exchange("/coupon/expire", HttpMethod.GET, request,
				ReturnDefault.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getResult()).isNotNull();
	}
}
