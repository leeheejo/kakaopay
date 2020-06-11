package com.kakaopay.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kakaopay.model.Coupon;
import com.kakaopay.repo.CouponRepository;
import com.kakaopay.service.CouponService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CouponServiceTests {
	private static boolean setUpFlag = false;
	// 7월 9일까지 테스트용 토큰 발급
	private static String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTQyNzQ5NjksInVzZXJJZCI6IlpFZFdlbVJGYkd0T1ZGa3oiLCJpYXQiOjE1OTE2ODI3OTMyMjB9.U2fBICMxqVXedM2BQwhFZ_jClGVBqAPg5z4YrYVvK4o";

	@Autowired
	CouponService couponService;

	@Autowired
	CouponRepository couponRepo;

	@Before
	public void init() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		if (setUpFlag) {
			return;
		}

		couponService.generateCoupon(5L);
		List<Coupon> testList = couponRepo.findAll();
		assertThat(testList.size(), CoreMatchers.is(5));
		String couponNumForTest = couponService.issueCoupon(token);
		assertThat(couponNumForTest).isNotBlank();
		setUpFlag = true;
	}

	@Test
	public void generateCoupon() throws Exception {
		couponService.generateCoupon(10L);
		assertThat(couponRepo.findAll().size()).isEqualTo(15);
	}

	@Test
	public void issueCoupon() throws Exception {
		String coupon = couponService.issueCoupon(token);
		assertThat(coupon).isNotBlank();
		assertThat(coupon.length()).isEqualTo(20);
	}

	@Test
	public void findIssuedCoupon() throws Exception {
		List<Coupon> list = couponService.findIssuedCoupon();
		assertThat(list.size()).isGreaterThanOrEqualTo(1);
		for (Coupon c : list) {
			assertThat(c.isIssued()).isEqualTo(true);
		}
	}

	@Test
	public void changeUseCoupon() throws Exception {
		String coupon = couponService.issueCoupon(token);
		couponService.changeUseCoupon(token, coupon, false);
		Coupon c = couponRepo.findOneByCoupon(coupon);
		assertThat(c.isUsed()).isEqualTo(true);

		couponService.changeUseCoupon(token, coupon, true);
		c = couponRepo.findOneByCoupon(coupon);
		assertThat(c.isUsed()).isEqualTo(false);
	}

	@Test
	public void getExpireTodayCoupon() throws Exception {
		List<Coupon> coupons = couponService.getExpireTodayCoupon();
		for (Coupon c : coupons) {
			assertThat(c.getExpiredAt().isAfter(LocalDate.now().atStartOfDay()));
			assertThat(c.getExpiredAt().isBefore(LocalDate.now().atTime(LocalTime.MAX)));
		}
	}

}
