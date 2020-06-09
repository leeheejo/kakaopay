package com.kakaopay.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.constant.Constant;
import com.kakaopay.exeption.ExpiredCouponException;
import com.kakaopay.exeption.InvalidTokenException;
import com.kakaopay.model.Coupon;
import com.kakaopay.repo.CouponRepository;
import com.kakaopay.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {

	private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

	@Autowired
	CouponRepository couponRepo;

	@Override
	@Transactional
	public void generateCoupon(Long N) {
		// TODO Auto-generated method stub
		Long sameCount = N;
		do {
			List<Coupon> test = new ArrayList<>();
			for (int i = 0; i < sameCount; i++) {
				test.add(new Coupon(createCouponNum()));
			}
			List<Coupon> res = new ArrayList<>();
			res = couponRepo.saveAll(test);
			for (Coupon c : res) {
				if (c.getCreatedAt() != null) {
					sameCount--;
				}
			}
			logger.info(res.toString());
		} while (sameCount != 0);

	}

	@Override
	@Transactional
	public String issueCoupon() {
		// TODO Auto-generated method stub
		Coupon coupon = couponRepo.findTop1ByIsIssued(false);

		if (coupon == null)
			throw new EntityNotFoundException(Constant.RES.NO_COUPON.getMessage());

		coupon.setIssued(true);
		coupon.setIssuedAt(LocalDateTime.now());
		couponRepo.save(coupon);
		logger.info(coupon.toString());
		return coupon.getCoupon();
	}

	@Override
	@Transactional
	public List<Coupon> findIssuedCoupon() {
		// TODO Auto-generated method stub
		return couponRepo.findByIsIssued(true);
	}

	@Override
	@Transactional
	public void changeUseCoupon(String coupon, boolean currentUseStatus) throws ExpiredCouponException {
		// currentUseStatus == true : 취소 처리 currentUseStatus == false면 사용처리
		Coupon c = couponRepo.findOneByCouponAndIsIssuedAndIsUsed(coupon, true, currentUseStatus);

		if (c == null)
			throw new EntityNotFoundException(Constant.RES.INVALID_COUPON.getMessage());

		if (c.getExpiredAt().isBefore(LocalDateTime.now()))
			throw new ExpiredCouponException(Constant.RES.EXPIRED_COUPON.getMessage());

		c.setUsed(!currentUseStatus);
		couponRepo.save(c);
	}

	@Override
	public List<Coupon> getExpireTodayCoupon() {
		// TODO Auto-generated method stub
		LocalDateTime start = LocalDate.now().atStartOfDay();
		LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
		logger.info(start.toString() + " " + end.toString());
		boolean useStatus = false;
		List<Coupon> expiredToday = couponRepo.findByExpiredAtBetweenAndIsUsed(start, end, useStatus);
		logger.info(expiredToday.toString());
		return expiredToday;
	}

	private String createCouponNum() {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 20; i++) {
			int rIndex = rnd.nextInt(3);
			switch (rIndex) {
			case 0:
				// a-z
				temp.append((char) ((int) (rnd.nextInt(26)) + 97));
				break;
			case 1:
				// A-Z
				temp.append((char) ((int) (rnd.nextInt(26)) + 65));
				break;
			case 2:
				// 0-9
				temp.append((rnd.nextInt(10)));
				break;
			}
		}

		return temp.toString();
	}

}
