package com.kakaopay.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

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

	@Autowired
	CouponService couponService;

	@Autowired
	CouponRepository couponRepo;

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
		couponService.generateCoupon(10L);
		assertThat(couponRepo.findAll().size()).isEqualTo(15);
	}

	@Test
	public void issueCoupon() throws Exception {
		String coupon = couponService.issueCoupon();
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
		String coupon = couponService.issueCoupon();
		couponService.changeUseCoupon(coupon, false);
		Coupon c = couponRepo.findOneByCoupon(coupon);
		assertThat(c.isUsed()).isEqualTo(true);

		couponService.changeUseCoupon(coupon, true);
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
