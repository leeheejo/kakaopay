package com.kakaopay.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.constant.Constant;
import com.kakaopay.exception.ExpiredCouponException;
import com.kakaopay.exception.InvalidUserException;
import com.kakaopay.model.Coupon;
import com.kakaopay.repo.CouponRepository;
import com.kakaopay.service.CouponService;
import com.kakaopay.utils.JWTUtils;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	CouponRepository couponRepo;

	@Override
	@Transactional
	public void generateCoupon(Long N) {
		// TODO Auto-generated method stub
		Long sameCount = N;
		do {
			List<Coupon> list = new ArrayList<>();
			for (int i = 0; i < sameCount; i++) {
				list.add(new Coupon(createCouponNum()));
			}
			List<Coupon> res = new ArrayList<>();
			res = couponRepo.saveAll(list);
			for (Coupon c : res) {
				if (c.getCreatedAt() != null) {
					sameCount--;
				}
			}
		} while (sameCount != 0);

	}

	@Override
	@Transactional
	public String issueCoupon(String token)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		// TODO Auto-generated method stub
		String userId = JWTUtils.getUserIdFromToken(token);
		Coupon coupon = couponRepo.findTop1ByIsIssued(false);

		if (coupon == null)
			throw new EntityNotFoundException(Constant.RES.NO_COUPON.getMessage());

		coupon.setIssued(true);
		coupon.setIssuedAt(LocalDateTime.now());
		coupon.setUserId(userId);
		couponRepo.save(coupon);
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
	public void changeUseCoupon(String token, String coupon, boolean currentUseStatus) throws ExpiredCouponException,
			UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException, InvalidUserException {
		// currentUseStatus == true : 취소 처리 currentUseStatus == false면 사용처리
		Coupon c = couponRepo.findOneByCouponAndIsIssuedAndIsUsed(coupon, true, currentUseStatus);

		if (c == null)
			throw new EntityNotFoundException(Constant.RES.INVALID_COUPON.getMessage());

		String userId = JWTUtils.getUserIdFromToken(token);
		if (!c.getUserId().equals(userId))
			throw new InvalidUserException(Constant.RES.USERID_NOT_MATCH.getMessage());

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
		boolean useStatus = false;
		List<Coupon> expiredToday = couponRepo.findByExpiredAtBetweenAndIsUsed(start, end, useStatus);
		return expiredToday;
	}

	private String createCouponNum() {
		StringBuffer num = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 20; i++) {
			int idx = rnd.nextInt(3);
			switch (idx) {
			case 0:// a-z
				num.append((char) ((int) (rnd.nextInt(26)) + 97));
				break;
			case 1:// A-Z
				num.append((char) ((int) (rnd.nextInt(26)) + 65));
				break;
			case 2:// 0-9
				num.append((rnd.nextInt(10)));
				break;
			}
		}

		return num.toString();
	}

}
