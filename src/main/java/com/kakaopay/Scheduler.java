package com.kakaopay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.kakaopay.model.Coupon;
import com.kakaopay.repo.CouponRepository;

@Component
public class Scheduler {
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	@Autowired
	CouponRepository couponRepo;

	@Scheduled(cron = "0 0 0 * * * ")
//	@Scheduled(fixedDelay = 3000)
	public void checkCouponExpire() {
		LocalDateTime start = LocalDate.now().atStartOfDay();
		LocalDateTime end = LocalDate.now().plusDays(3).atTime(LocalTime.MAX);
		List<Coupon> coupons = couponRepo.findByExpiredAtBetweenAndIsUsed(start, end, false);
		for (Coupon c : coupons) {
			logger.info("[" + c.getCoupon() + "] 쿠폰이 3일 후 만료됩니다.");
		}
	}

}
