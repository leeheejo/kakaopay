package com.kakaopay.exception;

public class ExpiredCouponException extends Exception {
	public ExpiredCouponException(String msg) {
		super(msg);
	}
}
