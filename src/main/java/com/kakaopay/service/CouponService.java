package com.kakaopay.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.kakaopay.exception.ExpiredCouponException;
import com.kakaopay.exception.InvalidTokenException;
import com.kakaopay.exception.InvalidUserException;
import com.kakaopay.model.Coupon;

public interface CouponService {

	public void generateCoupon(Long N);

	public String issueCoupon(String token)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException;

	public List<Coupon> findIssuedCoupon();

	public void changeUseCoupon(String token, String coupon, boolean currentUseStatus) throws ExpiredCouponException,
			UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException, InvalidUserException;

	public List<Coupon> getExpireTodayCoupon();

}
