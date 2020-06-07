package com.kakaopay.exeption;

public class ExpiredCouponException extends Exception {
	public ExpiredCouponException(String msg) {
		super(msg);
	}
}
