package com.kakaopay.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnCouponNum {

	private String coupon;

	@Builder
	public ReturnCouponNum(String coupon) {
		super();
		this.coupon = coupon;
	}

}
