package com.kakaopay.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

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

	private static String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjI1OTIwMCwidXNlcklkIjoiWkVkV2VtUkZiR3M9IiwiaWF0IjoxNTkxNTczMDgwfQ.9lxDVmTIdgNfIN--_Fu8_vYbM8FtGR6Ie1QQAuRR65g";

	@Before
	public void init() {
		if (setUpFlag) {
			return;
		}

		couponService.generateCoupon(5L);
		List<Coupon> testList = couponRepo.findAll();
		assertThat(testList.size(), CoreMatchers.is(5));
		String couponNumForTest = couponService.issueCoupon();
		assertThat(couponNumForTest).isNotBlank();
		setUpFlag = true;
	}

	@Test
	public void generateCoupon() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("Authorization", token);
		System.out.println(headers.toString());
		Map<String, String> map = new HashMap<>();
		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("/coupon/10", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(couponRepo.findAll().size()).isEqualTo(15);
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
		ResponseEntity<CouponListDTO> response = restTemplate.exchange("/coupon/issue", HttpMethod.GET, request,
				CouponListDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.out.println(response.getBody().toString());
		assertThat(response.getBody().getTotalCount()).isGreaterThanOrEqualTo(1);
	}

	@Test
	public void useCoupon() throws Exception {
		String couponNumForTest = couponService.issueCoupon();
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
		String couponNumForTest = couponService.issueCoupon();
		couponService.changeUseCoupon(couponNumForTest, false);
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
		ResponseEntity<CouponListDTO> response = restTemplate.exchange("/coupon/expire", HttpMethod.GET, request,
				CouponListDTO.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTotalCount()).isEqualTo(0);
		List<Coupon> coupons = response.getBody().getCoupons();
		for (Coupon c : coupons) {
			assertThat(c.getExpiredAt().isAfter(LocalDate.now().atStartOfDay()));
			assertThat(c.getExpiredAt().isBefore(LocalDate.now().atTime(LocalTime.MAX)));
		}
	}

}
