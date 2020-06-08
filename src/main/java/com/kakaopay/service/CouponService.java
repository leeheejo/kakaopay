package com.kakaopay.service;

import java.util.List;

import com.kakaopay.exeption.ExpiredCouponException;
import com.kakaopay.exeption.InvalidTokenException;
import com.kakaopay.model.Coupon;

public interface CouponService {

	public void generateCoupon(Long N);

	public String issueCoupon();

	public List<Coupon> findIssuedCoupon();

	public void changeUseCoupon(String coupon, boolean currentUseStatus)
			throws ExpiredCouponException;

	public List<Coupon> getExpireTodayCoupon();

}
